package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.models.ProductPackageTb;
import parkingos.com.bolink.service.ProductPackageService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductPackageAction {

    Logger logger = LoggerFactory.getLogger(ProductPackageAction.class);

    @Autowired
    private ProductPackageService productPackageService;
    @Autowired
    private SaveLogService saveLogService;


    @RequestMapping(value = "/query")
    public String query(HttpServletRequest request, HttpServletResponse resp){

        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

        JSONObject result = productPackageService.selectResultByConditions(reqParameterMap);
        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/add")
    public String add(HttpServletRequest request, HttpServletResponse resp){
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        //创建时间
        Long createTime = System.currentTimeMillis()/1000;
        //车场编号
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        String period = RequestUtil.getString(request, "period");
        String carTypeId = RequestUtil.getString(request,"car_type_id");
        Double price = RequestUtil.getDouble(request, "price", 0.0);
        //月卡名称
        String p_name = RequestUtil.processParams(request, "p_name");
        //月卡描述
        String describe =RequestUtil.getString(request, "describe");
        //生成唯一主键
        Long id = productPackageService.getId();

        BigDecimal bigDecimal = new BigDecimal(price+"");

        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setComid(comid);
        productPackageTb.setId(id);
        productPackageTb.setIsDelete(0L);
        productPackageTb.setPeriod(period);
        productPackageTb.setCardId(id+"");
        productPackageTb.setCarTypeId(carTypeId);
        productPackageTb.setPrice(bigDecimal);
        productPackageTb.setpName(p_name);
        productPackageTb.setDescribe(describe);
        productPackageTb.setCreateTime(createTime);
        JSONObject result = productPackageService.createProduct(productPackageTb);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(1);
            parkLogTb.setContent(uin+"("+nickname+")"+"创建了月卡套餐"+id+p_name);
            parkLogTb.setType("product");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }

        //把结果返回页面
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/delete")
    public String delete(HttpServletRequest request, HttpServletResponse resp){
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);

        Long id =RequestUtil.getLong(request, "id", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);

        JSONObject result = productPackageService.deleteProduct(id,comid);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(3);
            parkLogTb.setContent(uin+"("+nickname+")"+"删除了月卡套餐"+id);
            parkLogTb.setType("product");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }

    @RequestMapping(value = "/edit")
    public String edit(HttpServletRequest request, HttpServletResponse resp){
        String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
        Long uin = RequestUtil.getLong(request, "loginuin", -1L);
        Long comid = RequestUtil.getLong(request, "comid", -1L);
        Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
        JSONObject result = productPackageService.editProduct(reqParameterMap);
        if((Integer)result.get("state")==1){
            ParkLogTb parkLogTb = new ParkLogTb();
            parkLogTb.setOperateUser(nickname);
            parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
            parkLogTb.setOperateType(2);
            parkLogTb.setContent(uin+"("+nickname+")"+"修改了月卡套餐"+reqParameterMap.get("id"));
            parkLogTb.setType("product");
            parkLogTb.setParkId(comid);
            saveLogService.saveLog(parkLogTb);
        }
        StringUtils.ajaxOutput(resp,result.toJSONString());
        return null;
    }
}
