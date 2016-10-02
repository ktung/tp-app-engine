package fr.unice.polytech.sacc.tp1;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;
import fr.unice.polytech.sacc.tp1.model.Article;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PushEngine extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Thread.sleep(500);

            String queueName = request.getHeader("X-AppEngine-QueueName");
            String taskName = request.getHeader("X-AppEngine-TaskName");
            Queue queue = QueueFactory.getQueue(queueName);

            Gson gson = new Gson();
            Article article = gson.fromJson(request.getParameter("article"), Article.class);
            response.setContentType("application/json");
            response.getWriter().println(gson.toJson(article));
            queue.deleteTask(taskName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Article article = new Article("Trésor", 8.00, 1);

        Gson gson = new Gson();
        Queue queue = QueueFactory.getQueue("push-queue");
        queue.add(TaskOptions.Builder.withUrl("/push")
            .method(TaskOptions.Method.POST)
            .param("article", gson.toJson(article))
        );

        response.setContentType("text/plain");
        response.getWriter().println("Article added to the queue");
    }
}
