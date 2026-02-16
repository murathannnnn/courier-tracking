<div align="center">
  <h1 style="font-size: 3rem; border-bottom: none;">
    <b>🚚 COURIER TRACKING SYSTEM</b>
  </h1>
  <p>
    <i>A Technical Case Study Implementation for Courier Tracking System.</i>
  </p>
</div>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  📝 Application Description
</h2>

<p>
  This project is a <b>RESTful Web Application</b> developed as a technical case study.
  The primary objective is to process streaming geolocation data from couriers and
  manage their interactions with predefined store locations.
</p>

<p>
  The application is built following the <b>Hexagonal Architecture (Ports and Adapters)</b>
  pattern to ensure a highly decoupled, maintainable, and testable codebase.
</p>

<h3>🎯 Core Functionalities</h3>

<ul>
  <li>
    <b>📍 Real-Time Geofencing:</b>
    Automatically detects when a courier enters a 100-meter radius of any Migros store
    (loaded from <code>stores.json</code>).
  </li>
  <li>
    <b>🛡️ Smart Entry Logging:</b>
    Implements a 1-minute timeout rule for re-entries. Multiple entries by the same
    courier into the same store's circumference within 60 seconds are filtered to
    maintain data integrity.
  </li>
  <li>
    <b>📏 Distance Tracking:</b>
    Tracks and persists the total travel distance for each courier based on their
    sequential geolocation updates.
  </li>
  <li>
    <b>🔍 Data Querying:</b>
    Provides an API endpoint to retrieve the total distance traveled by a specific
    courier (<code>getTotalTravelDistance</code>).
  </li>
</ul>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  🛠 Tech Stack
</h2>

<ul>
  <li><b>Java 21</b></li>
  <li><b>Spring Boot 3.4.1</b></li>
  <li><b>Redis</b></li>
  <li><b>H2 Database</b></li>
  <li><b>Spring Data JPA</b></li>
  <li><b>Docker & Docker Compose</b></li>
  <li><b>JUnit 5 & Mockito</b></li>
  <li><b>Lombok</b></li>
</ul>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  🚀 Setup & Run Instructions
</h2>

<h3>📋 Prerequisites</h3>

<p>
  To run this application, you need the following tools installed on your system:
</p>

<ul>
  <li>
    <b>Docker & Docker Compose:</b>
    Required to containerize the application and run the Redis instance.
  </li>
  <li>
    <b>Postman (Optional):</b>
    Recommended for testing the API endpoints.
  </li>
  <li>
    <b>Java 21 & Maven 3.9+ (Optional):</b>
    Only required if you wish to build or run the project outside of Docker.
  </li>
</ul>

<h3>🐳 Running with Docker</h3>

<p>
  The easiest way to start the system is using Docker Compose.
  This will spin up the Java application and the Redis cache automatically.
</p>

<ol>
  <li>Open your terminal in the project root directory.</li>
  <li>Run the following command:</li>
</ol>

<pre><code>docker-compose up --build</code></pre>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  🚀 One-Click Start (Recommended)
</h2>

<p>
  You can start the application without using the terminal by simply
  double-clicking the appropriate start script for your operating system.
</p>

<h3>🍎 macOS</h3>

<ol>
  <li>Ensure <b>Docker Desktop</b> is running.</li>
  <li>Double click <code>start.command</code></li>
</ol>

<h3>🪟 Windows</h3>

<ol>
  <li>Ensure <b>Docker Desktop</b> is running.</li>
  <li>Double click <code>start.bat</code></li>
</ol>

<p>
  This will automatically build and start the application along with Redis
  using Docker Compose.
</p>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  🧪 How to Test the Application
</h2>

<p>
  Follow the steps below to test the system end-to-end using the provided
  Postman Collection.
</p>

<h3>1️⃣ Import the Postman Collection</h3>

<ol>
  <li>Download the provided <b>Postman Collection</b> from the repository.</li>
  <li>Open <b>Postman</b>.</li>
  <li>Click <b>Import</b> and select the collection file.</li>
</ol>

<h3>2️⃣ Create a Courier</h3>

<ol>
  <li>Open the <b>Create Courier</b> request (POST).</li>
  <li>Send the request to create a new courier in the system.</li>
  <li>Copy the returned <code>courierId</code> from the response.</li>
</ol>

<h3>3️⃣ Track Courier Location (Initial Entry)</h3>

<ol>
  <li>Open the <b>Track Courier Location</b> request.</li>
  <li>Enter the <code>courierId</code> you created.</li>
  <li>Send the request.</li>
</ol>

<p>
  By default, the request body is configured with the coordinates of the
  <b>Ataşehir Migros Store</b>. This should trigger a store entry event if the
  courier is within the 100-meter radius.
</p>

<h3>4️⃣ Test Distance Calculation</h3>

<ol>
  <li>Modify the latitude and longitude in the <b>Track Courier Location</b> request.</li>
  <li>Use coordinates of a different Migros store.</li>
  <li>Send the request again.</li>
</ol>

<p>
  Now call the <b>Get Total Travel Distance</b> endpoint to verify that the
  total traveled distance has increased based on the new location update.
</p>

<h3>5️⃣ Verify the 1-Minute Re-Entry Rule</h3>

<ol>
  <li>Send another <b>Track Courier Location</b> request using the same store coordinates.</li>
  <li>Wait only 20–30 seconds.</li>
  <li>Send the request again.</li>
</ol>

<p>
  The system should <b>NOT</b> log a new store entry because of the
  1-minute re-entry restriction rule. This validates that duplicate entries
  within 60 seconds are properly filtered.
</p>

<p>✅ At this point, you will have verified:</p>

<ul>
  <li>Courier creation</li>
  <li>Geofencing logic (100m radius detection)</li>
  <li>Distance calculation</li>
  <li>Re-entry timeout rule (data integrity protection)</li>
</ul>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  📘 API Documentation (Swagger UI)
</h2>

<p>
  The application provides interactive API documentation using <b>Swagger UI</b>.
</p>

<h3>🔗 Accessing Swagger</h3>

<ol>
  <li>Make sure the application is running (via Docker or One-Click Start).</li>
  <li>Open your browser.</li>
  <li>Navigate to:</li>
</ol>

<pre><code>http://localhost:8080/swagger-ui/index.html</code></pre>

<p>You will see the interactive Swagger interface where you can:</p>

<ul>
  <li>View all available REST endpoints</li>
  <li>Inspect request/response schemas</li>
  <li>Execute API calls directly from the browser</li>
  <li>Validate request payload structures</li>
</ul>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  💎 Design Patterns Used
</h2>

<h3>🎯 Strategy Pattern</h3>

<p><b>Used for:</b> Distance calculation logic.</p>
<p>
  Since calculating distances between coordinates can be done via various
  mathematical formulas (Haversine, Vicenty, etc.), the logic is encapsulated
  within a strategy interface.
</p>
<p>
  <b>Result:</b> The system can switch or add new calculation models at runtime
  without modifying the core domain service.
</p>

<h3>🔔 Observer Pattern</h3>

<p><b>Used for:</b> Store Entry Event mechanism.</p>
<p>
  When a courier enters a 100m radius of a store, several decoupled actions may
  need to occur (logging to DB, updating cache, sending notifications).
</p>
<p>
  <b>Result:</b> By using Spring Application Events, the detection logic is
  separated from the action logic. The system publishes an event, and multiple
  listeners (Observers) can handle it independently.
</p>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  ⚡ Cache & Eviction Strategy
</h2>

<p>
  The application uses <b>Redis</b> as a distributed cache to strictly enforce
  the "one entry per minute" rule with maximum performance.
</p>

<ul>
  <li>
    <b>Logic:</b> When a courier enters the 100-meter radius of a store,
    the system generates a unique cache key:
    <code>courier:{courierId}:store:{storeId}</code>.
  </li>
  <li>
    <b>1-Minute TTL Rule:</b> If this key does not exist in Redis, the entry
    is logged to the database, and the key is saved with a
    <b>60-second Time-To-Live (TTL)</b>.
  </li>
  <li>
    <b>Cache Eviction:</b>
    <ul>
      <li>If the courier re-enters within 60 seconds, the request is ignored.</li>
      <li>After 60 seconds, Redis automatically deletes the key.</li>
      <li>The next update is treated as a new valid entrance.</li>
    </ul>
  </li>
  <li>
    <b>Performance:</b> Prevents redundant database I/O and keeps the
    100-meter radius check fast under high traffic.
  </li>
</ul>

<hr>

<h2 style="border-bottom: 2px solid #eaecef; padding-bottom: 8px;">
  🧪 Unit Tests
</h2>

<p>
  The project maintains a high standard of code quality through a comprehensive
  testing suite focusing primarily on the <b>Domain</b> and <b>Application</b> layers.
</p>

<ul>
  <li>
    <b>Domain Logic:</b> Mathematical calculations (Haversine) and core business
    rules (100m radius check) are fully covered.
  </li>
  <li>
    <b>Isolation:</b> Mockito is used to mock external dependencies (Redis, JPA),
    ensuring fast and focused unit tests.
  </li>
</ul>

<p><b>How to Run Tests:</b></p>

<pre><code>./mvnw test</code></pre>

<hr>
