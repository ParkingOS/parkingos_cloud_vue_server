package parkingos.com.bolink.service;

import javax.servlet.http.HttpServletRequest;

public interface ShopManageService {
    String create(HttpServletRequest request);

    String delete(HttpServletRequest request);

    String quickquery(HttpServletRequest req);

    String addMoney(HttpServletRequest request);
}
