package com.apin.special.ticket.controller;

import com.apin.special.ticket.service.SpecialTicketService;
import com.apin.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by Administrator on 2016/8/14.
 */
@Controller
public class SpecialTicketController {

    private static final Logger logger = LoggerFactory.getLogger(SpecialTicketController.class);

    @Autowired
    private SpecialTicketService specialTicketService;

    /**
     * 获取特价机票
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws Throwable
     */
    @ResponseBody
    @RequestMapping("/api/sharePlane/fare/ticket/get")
    public ApinResponse getHomeFlights(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "pageNo", required = false) String pageNo,
            @RequestParam(value = "pageSize", required = false) String pageSize
    ) throws Throwable {
        long startTime = System.currentTimeMillis();

        if (ApinUtil.isBlank(pageNo, pageSize)) {
            return ApinResponseUtil.bad(startTime, ErrorEnum.ERROR_PARAM_ISBLANK);
        }

        ApinResultModel model = new ApinResultModel();
        try {
            specialTicketService.getSpecialTicketFlights(model, userId, Integer.parseInt(pageNo), Integer.parseInt(pageSize));
            return ApinResponseUtil.good(startTime, model.getData());
        } catch (Throwable e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            if (StringUtils.equals(model.getErrorCode(), "00000000")) {
                return ApinResponseUtil.bad(startTime, ErrorEnum.ERROR_INTERNAL_SERVER_ERROR);
            } else {
                return ApinResponseUtil.bad(startTime, model.getErrorCode(), model.getErrorMsg());
            }
        }
    }

}
