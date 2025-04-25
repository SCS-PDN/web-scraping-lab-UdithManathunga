import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Arrays;

@WebServlet("/ScraperServlet")
public class ScraperServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getParameter("url");
        String[] options = request.getParameterValues("option");

        boolean getTitle = false, getHeadings = false, getLinks = false;

        if (options != null) {
            getTitle = Arrays.asList(options).contains("title");
            getHeadings = Arrays.asList(options).contains("headings");
            getLinks = Arrays.asList(options).contains("links");
        }

        NewsData data = WebScraper1.scrape(url, getTitle, getHeadings, getLinks);

        // Session Tracking
        HttpSession session = request.getSession();
        Integer visitCount = (Integer) session.getAttribute("visitCount");
        if (visitCount == null) visitCount = 0;
        session.setAttribute("visitCount", ++visitCount);

        // Convert to JSON
        Gson gson = new Gson();
        String json = gson.toJson(data);

        // Output JSON with visit count
        response.setContentType("application/json");
        response.getWriter().write("{\"visitCount\": " + visitCount + ", \"data\": " + json + "}");
    }
}
