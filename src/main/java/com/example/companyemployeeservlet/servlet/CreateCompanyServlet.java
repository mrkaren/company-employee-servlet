package com.example.companyemployeeservlet.servlet;

import com.example.companyemployeeservlet.manager.CompanyManager;
import com.example.companyemployeeservlet.model.Company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebServlet("/createCompany")
public class CreateCompanyServlet extends HttpServlet {

    private CompanyManager companyManager = new CompanyManager();
    List<String> countries = Arrays.asList("Armenia", "USA", "Russia", "France");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("countryList", countries);
        req.getRequestDispatcher("WEB-INF/createCompany.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String country = req.getParameter("country");
        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        companyManager.save(company);
        resp.sendRedirect("/companies");
    }
}
