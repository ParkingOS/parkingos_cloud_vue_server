package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarowerProduct;
import parkingos.com.bolink.service.VipService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/vip")
public class VipManageAction {

    Logger logger = Logger.getLogger(CarRenewAction.class);


    @Autowired
    private VipService vipService;

    @Autowired
    private CommonDao commonDao;

    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = vipService.selectResultByConditions(reqParameterMap);

        StringUtils.ajaxOutput(response,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "edit")
    public String edit(HttpServletRequest req, HttpServletResponse resp){
        Long id = RequestUtil.getLong(req, "id", -1L);

        if(id == -1){
            StringUtils.ajaxOutput(resp,"-1");
            return null;
        }
        return null;
    }


    @RequestMapping(value = "add")
    public String add(HttpServletRequest req, HttpServletResponse resp){
        Long pid =RequestUtil.getLong(req, "p_name",-1L);
        //车主手机
        String mobile =StringUtils.decodeUTF8(RequestUtil.processParams(req, "mobile").trim());
        String name = StringUtils.decodeUTF8(RequestUtil.processParams(req, "name").trim());
        String address = StringUtils.decodeUTF8(RequestUtil.processParams(req, "address").trim());
        //车牌号码
        //String car_number =AjaxUtil.decodeUTF8(RequestUtil.processParams(request, "car_number")).toUpperCase();
        //起始时间
        String b_time =RequestUtil.processParams(req, "b_time");
        //购买月数
        Integer months = RequestUtil.getInteger(req, "months", 1);
        //生成id
        //修改月卡会员编号cardId为主键id
        Long nextid = vipService.getkey();

        String cardId = String.valueOf(nextid);

        CarowerProduct carowerProduct = new CarowerProduct();
        carowerProduct.setComId(21782L);
        carowerProduct.setCardId(cardId);
        carowerProduct.setIsDelete(0L);
        int count = vipService.selectCountByConditions(carowerProduct);//getLong("select count(ID) from carower_product where com_id=? and card_id=? and is_delete =?  ", new Object[]{comid,cardId,0});
        if(count>0){
            return "-3";
        }

        return null;
    }
}
