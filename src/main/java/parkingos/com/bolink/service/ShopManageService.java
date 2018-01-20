package parkingos.com.bolink.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ShopManageService {
    String create(HttpServletRequest request, HttpServletResponse resp);

    String delete(HttpServletRequest request, HttpServletResponse resp);

    String quickquery(HttpServletRequest req, HttpServletResponse resp);

    String addMoney(HttpServletRequest request, HttpServletResponse resp);
}
