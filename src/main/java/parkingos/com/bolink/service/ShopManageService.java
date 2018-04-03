package parkingos.com.bolink.service;

import parkingos.com.bolink.models.ComInfoTb;

import javax.servlet.http.HttpServletRequest;

public interface ShopManageService {
    String create(HttpServletRequest request);

    String delete(HttpServletRequest request);

    String quickquery(HttpServletRequest req);

    String addMoney(HttpServletRequest request);

    int updateComSuperimposed(ComInfoTb comInfoTb);
}
