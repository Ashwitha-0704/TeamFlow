# Frontend Files Directory

The frontend files for the **TeamFlow** application are integrated directly into the Spring Boot backend project layout to enable Thymeleaf templates and direct routing.

Here are the locations of all your frontend files:

1. **HTML Templates (Views)**:
   - Located in: `TeamFlow/src/main/resources/templates/`
   - Files include:
     - [dashboard.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/dashboard.html)
     - [tasks.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/tasks.html) (Modified to include real-time filters and search!)
     - [projects.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/projects.html)
     - [login.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/login.html)
     - [users.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/users.html)
     - [incidents.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/incidents.html)
     - [reviews.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/reviews.html)
     - [notifications.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/notifications.html)
     - [reports.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/reports.html)
     - Layout Fragment: [fragments/layout.html](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/templates/fragments/layout.html)

2. **JavaScript File (API Calls and Logic)**:
   - Located in: [app.js](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/static/js/app.js)

3. **CSS Stylesheet (Theme and Styling)**:
   - Located in: [style.css](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/resources/static/css/style.css)

---

## Example of How Frontend Connects to Backend

Your JavaScript file `app.js` handles communicating with backend REST endpoints using the `fetch` API.

For example, when fetching tasks on the `tasks.html` page:
1. **HTML/JS Trigger**: `DOMContentLoaded` triggers `loadTasks()`
2. **Fetch Request**:
   ```javascript
   // Inside app.js
   async function apiCall(url, method = 'GET', body = null) {
       ...
       const response = await fetch('/api' + url, options);
       return await response.json();
   }
   ```
3. **Backend Service**: This triggers the `@RestController` mapped to `@RequestMapping("/api/tasks")` in [TaskController.java](file:///c:/Users/ashwi/OneDrive/Desktop/8element/TeamFlow/src/main/java/com/example/teamflow/controller/TaskController.java):
   ```java
   @GetMapping
   public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
       return ResponseEntity.ok(taskService.getAllTasks());
   }
   ```
4. **Rendering**: The JSON list of tasks returned from Spring Boot is filtered and populated dynamically in the HTML table `<tbody>` via DOM manipulation.
