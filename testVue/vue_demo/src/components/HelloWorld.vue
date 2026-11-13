<template>
  <div class="hello-container">
    <h1>Hello World! 🎉</h1>
    <p>Congratulations, you have logged in successfully!</p>

    <!-- Action buttons container -->
    <div class="button-container">
      <button @click="goBack" class="btn">Return to login page</button>
      <button @click="openCourseApi" class="btn">Open Course Api</button>
      <button @click="fetchCourses" :disabled="loading" class="btn">
        {{ loading ? "Loading..." : "Load Courses" }}
      </button>
    </div>

    <h2>Course List</h2>

    <!-- Visible Error / Status Message -->
    <p v-if="statusMessage" class="status-message">{{ statusMessage }}</p>

    <!-- Modern Course Table -->
    <div v-if="courses.length > 0" class="table-wrapper">
      <table class="course-table">
        <thead>
          <tr>
            <th class="col-id">ID</th>
            <th class="col-code">Course Code</th>
            <th>Course Title</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="course in courses" :key="course.id">
            <td class="col-id">#{{ course.id }}</td>
            <td>
              <span class="course-badge">{{ course.code }}</span>
            </td>
            <td class="course-title">{{ course.title }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Empty State -->
    <div v-else-if="hasSearched && !loading">
      <p>No courses found in the database.</p>
    </div>

    <!-- Initial State -->
    <div v-else-if="!loading">
      <p>Click the button to load courses.</p>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";

const router = useRouter();

// Reactive state variables
const courses = ref([]);
const loading = ref(false);
const statusMessage = ref("");
const hasSearched = ref(false);

const goBack = () => {
  localStorage.clear();
  router.push("/");
};

const openCourseApi = () => {
  window.open("http://localhost:8080/api/courses", "_blank");
};

const fetchCourses = async () => {
  loading.value = true;
  statusMessage.value = "";
  try {
    const response = await fetch("http://localhost:8080/api/courses");
    if (response.status === 204) {
      courses.value = [];
      statusMessage.value = "No courses found in the database.";
      return;
    }
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }
    const data = await response.json();
    courses.value = data;
  } catch (error) {
    console.error("Error fetching courses:", error);
    statusMessage.value =
      "Failed to load courses. Check the browser console (F12) for CORS or network errors.";
  } finally {
    loading.value = false;
    hasSearched.value = true;
  }
};
</script>

<style scoped>
.hello-container {
  padding: 40px;
  text-align: center;
  max-width: 900px;
  margin: 0 auto;
}
.button-container {
  display: flex;
  gap: 15px;
  justify-content: center;
  padding: 20px 0;
}
.btn {
  padding: 8px 16px;
  cursor: pointer;
}
.status-message {
  color: #dc2626;
  font-weight: 600;
  margin: 12px 0;
}
.table-wrapper {
  margin-top: 20px;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid #e2e8f0;
  background-color: #ffffff;
}
.course-table {
  width: 100%;
  border-collapse: collapse;
  font-family: system-ui, -apple-system, sans-serif;
  text-align: left;
}
.course-table thead {
  background-color: #f8fafc;
  border-bottom: 2px solid #e2e8f0;
}
.course-table th {
  padding: 14px 18px;
  font-size: 0.8rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #475569;
}
.course-table td {
  padding: 14px 18px;
  border-bottom: 1px solid #f1f5f9;
  color: #1e293b;
  font-size: 0.95rem;
}
.course-table tbody tr:last-child td {
  border-bottom: none;
}
.course-table tbody tr:nth-child(even) {
  background-color: #fcfdfe;
}
.course-table tbody tr:hover {
  background-color: #f1f5f9;
  transition: background-color 0.15s ease;
}
.col-id {
  width: 80px;
  color: #64748b;
  font-weight: 500;
}
.col-code {
  width: 160px;
}
.course-badge {
  display: inline-block;
  padding: 4px 10px;
  background-color: #eff6ff;
  color: #1d4ed8;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  font-weight: 600;
  font-size: 0.85rem;
}
.course-title {
  font-weight: 500;
}
</style>