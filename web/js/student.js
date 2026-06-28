/* ════════════════════════════════════════════════════════════
   student.js  —  Girls Hostel Management System  v4.0
   Student Portal: profile, notices, rent, employees, facilities
════════════════════════════════════════════════════════════ */

window.onload = async function () {

    const username = localStorage.getItem("studentName");
    if (!username) { window.location.href = "login.html"; return; }

    // Topbar & sidebar names
    document.getElementById("topbarName").textContent = username;
    document.getElementById("sidebarName").textContent = username;
    document.getElementById("dashGreeting").textContent = "Hello, " + username + "! 👋";

    // ── Fetch student profile ──────────────────────────────────────
    try {
        const response = await fetch(
            "http://localhost:8080/getStudent?name=" + encodeURIComponent(username)
        );
        const student = await response.json();

        // Persist student code for complaint & leave modules
        localStorage.setItem("studentCode", student.studentCode || "");

        // Sidebar photo & code
        const sidePic = document.getElementById("sidebarPic");
        if (student.photo) {
            sidePic.innerHTML = `<img src="${student.photo}" alt="photo">`;
        }
        document.getElementById("sidebarCode").textContent = student.studentCode || "";

        // Dashboard stats
        document.getElementById("dashRoom").textContent = student.roomNo || "—";

        // Build full profile card
        const photoHtml = student.photo
            ? `<div class="profile-avatar"><img src="${student.photo}" alt="Photo"></div>`
            : `<div class="profile-avatar">👤</div>`;

        document.getElementById("profile").innerHTML = `
            <div class="profile-card">
                ${photoHtml}
                <div class="profile-details">
                    <div style="font-size:20px;font-weight:700;color:#0f2240;margin-bottom:4px;">
                        ${student.name || "—"}
                    </div>
                    <div style="display:inline-block;background:#dbeafe;color:#1d4ed8;
                                font-size:12px;font-weight:700;border-radius:20px;
                                padding:3px 12px;margin-bottom:14px;">
                        ${student.studentCode || "No Code"}
                    </div>
                    <table>
                        ${profileRow("👤 Occupation",   student.occupation  || "—")}
                        ${profileRow("📧 Email",        student.email       || "—")}
                        ${profileRow("📅 Date of Birth",student.dob         || "—")}
                        ${profileRow("🏠 Address",       student.address     || "—")}
                        ${profileRow("📊 Floor",         student.floor !== undefined ? student.floor : "—")}
                        ${profileRow("🛏 Room No.",      student.roomNo      || "—")}
                        ${profileRow("📅 Joining Date",  student.joiningDate || "—")}
                        ${profileRow("📞 Contact",       student.contact     || "—")}
                    </table>
                </div>
            </div>`;

    } catch(e) {
        console.warn("Profile load error:", e);
        document.getElementById("profile").innerHTML =
            `<p style="color:var(--muted)">Could not load profile. Is the server running?</p>`;
    }

   // Dashboard
await loadDashboardNotices();

// Individual pages
await loadNotices();
await loadRent();
await loadEmployees();
await loadFacilities();
await loadMyComplaints();
await loadMyLeaves();

// Dashboard cards
await updateDashboardComplaints();
await updateDashboardLeaves();
};

// ── Profile row helper ────────────────────────────────────────────
function profileRow(label, value) {
    return `<tr>
        <td style="padding:9px 12px;font-weight:600;color:#374151;width:40%;
                   border-bottom:1px solid #f1f5f9;">${label}</td>
        <td style="padding:9px 12px;color:#444;border-bottom:1px solid #f1f5f9;">${value}</td>
    </tr>`;
}

// ── Dashboard: quick notice preview ──────────────────────────────
async function loadDashboardNotices() {
    const el = document.getElementById("dashNotices");
    try {
        const r = await fetch("http://localhost:8080/notices");
        const d = await r.text();
        if (!d.trim()) { el.innerHTML = `<p style="color:var(--muted)">No notices at this time.</p>`; return; }
        const rows = d.trim().split("\n").slice(0, 2);
        el.innerHTML = rows.map(row => {
            const p = row.split("|");
            return `<div class="notice-card">
                <h4>${p[0] || ""}</h4>
                <p>${p[1] || ""}</p>
                <small>${p[2] || ""}</small>
            </div>`;
        }).join("");
    } catch(e) {
        el.innerHTML = `<p style="color:var(--muted)">Notices unavailable.</p>`;
    }
}

// ── Notices ────────────────────────────────────────────────────────
async function loadNotices() {
    const el = document.getElementById("noticeList");
    if (!el) return;
    el.innerHTML = `<p style="color:var(--muted)">Loading notices…</p>`;
    try {
        const r = await fetch("http://localhost:8080/notices");
        const d = await r.text();
        if (!d.trim()) { el.innerHTML = `<p style="color:var(--muted)">No notices available.</p>`; return; }
        const rows = d.trim().split("\n");
        el.innerHTML = rows.map(row => {
            const p = row.split("|");
            return `<div class="notice-card">
                <h4>${p[0] || ""}</h4>
                <p>${p[1] || ""}</p>
                <small>📅 ${p[2] || ""}</small>
            </div>`;
        }).join("");
    } catch(e) {
        el.innerHTML = `<p style="color:var(--muted)">Could not load notices.</p>`;
    }
}

// ── Rent ──────────────────────────────────────────────────────────
async function loadRent() {
    const el = document.getElementById("rentList");
    if (!el) return;
    el.innerHTML = `<p style="color:var(--muted)">Loading rent records…</p>`;
    const username = localStorage.getItem("studentName");
    try {
        const r = await fetch(
            "http://localhost:8080/rent?studentName=" + encodeURIComponent(username)
        );
        const d = await r.text();
        if (!d.trim()) {
            el.innerHTML = `<div class="card"><div class="card-body"><p style="color:var(--muted)">No rent records found.</p></div></div>`;
            return;
        }
        const rows = d.trim().split("\n");
        el.innerHTML = rows.map(row => {
            const p = row.split(",");
            const status = (p[6] || "").trim();
            const statusClass = status.toLowerCase() === "paid" ? "badge-green" : "badge-red";
            return `<div class="rent-card">
                <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:8px;">
                    <strong style="font-size:15px;">📅 ${p[0] || ""}</strong>
                    <span class="badge ${statusClass}">${status || "Pending"}</span>
                </div>
                <div style="display:grid;grid-template-columns:1fr 1fr;gap:4px 24px;font-size:13px;color:#374151;">
                    <span>🛏 Room No: <strong>${p[1] || "—"}</strong></span>
                    <span>🏠 Room Rent: <strong>₹${p[2] || "0"}</strong></span>
                    <span>⚡ Electric Bill: <strong>₹${p[3] || "0"}</strong></span>
                    <span>📊 Meter Reading: <strong>${p[4] || "—"}</strong></span>
                    <span style="grid-column:1/-1;margin-top:6px;font-size:15px;font-weight:700;color:#0f2240;">
                        Total: ₹${p[5] || "0"}
                    </span>
                </div>
            </div>`;
        }).join("");
    } catch(e) {
        el.innerHTML = `<div class="card"><div class="card-body"><p style="color:var(--muted)">Could not load rent data.</p></div></div>`;
    }
}

// ── Employees ─────────────────────────────────────────────────────
async function loadEmployees() {
    const el = document.getElementById("employees");
    if (!el) return;
    try {
        const r = await fetch("http://localhost:8080/viewEmployees");
        const d = await r.text();
        const rows = d.trim().split("\n").filter(r => r.trim());
        if (!rows.length) { el.innerHTML = `<p style="color:var(--muted)">No staff records found.</p>`; return; }

        el.innerHTML = `<div class="tbl-wrap"><table class="data-tbl" id="empTable">
            <thead><tr><th>Name</th><th>Role</th><th>Contact</th></tr></thead>
            <tbody id="empTableBody">
                ${rows.map(row => {
                    const p = row.split(",");
                    return `<tr><td><strong>${p[1] || "—"}</strong></td><td>${p[2] || "—"}</td><td>📞 ${p[3] || "—"}</td></tr>`;
                }).join("")}
            </tbody>
        </table></div>`;
    } catch(e) {
        el.innerHTML = `<p style="color:var(--muted)">Could not load staff directory.</p>`;
    }
}

// ── Facilities ────────────────────────────────────────────────────
async function loadFacilities() {
    const el = document.getElementById("facilities");
    if (!el) return;
    try {
        const r = await fetch("http://localhost:8080/viewFacilities");
        const d = await r.text();
        const rows = d.trim().split("\n").filter(r => r.trim());
        if (!rows.length) { el.innerHTML = `<p style="color:var(--muted)">No facilities listed.</p>`; return; }

        el.innerHTML = `<div class="tbl-wrap"><table class="data-tbl">
            <thead><tr><th>Facility</th><th>Timing</th><th>Description</th></tr></thead>
            <tbody>
                ${rows.map(row => {
                    const p = row.split(",");
                    return `<tr>
                        <td><strong>${p[1] || "—"}</strong></td>
                        <td>⏰ ${p[2] || "—"}</td>
                        <td>${p[3] || "—"}</td>
                    </tr>`;
                }).join("")}
            </tbody>
        </table></div>`;
    } catch(e) {
        el.innerHTML = `<p style="color:var(--muted)">Could not load facilities.</p>`;
    }
}

// ── Dashboard stats helpers ───────────────────────────────────────
async function updateDashboardComplaints() {
    try {
        const code = localStorage.getItem("studentCode") || "";
        const r = await fetch("http://localhost:8080/studentComplaints?code=" + encodeURIComponent(code));
        const d = await r.text();
        const count = d.trim() ? d.trim().split("\n").filter(x => x.trim()).length : 0;
        const el = document.getElementById("dashComplaints");
        if (el) el.textContent = count;
    } catch(e) {}
}

async function updateDashboardLeaves() {
    try {
        const code = localStorage.getItem("studentCode") || "";
        const r = await fetch("http://localhost:8080/studentLeaves?code=" + encodeURIComponent(code));
        const d = await r.text();
        const count = d.trim() ? d.trim().split("\n").filter(x => x.trim()).length : 0;
        const el = document.getElementById("dashLeaves");
        if (el) el.textContent = count;
    } catch(e) {
        const el = document.getElementById("dashLeaves");
        if (el) el.textContent = "—";
    }
}

// ── Complaints ────────────────────────────────────────────────────
async function submitComplaint() {
    const studentCode = localStorage.getItem("studentCode") || "";
    const studentName = localStorage.getItem("studentName") || "";
    const title       = document.getElementById("cTitle").value.trim();
    const category    = document.getElementById("cCategory").value;
    const description = document.getElementById("cDescription").value.trim();

    if (!title || !description) {
        alert("Please fill in Title and Description.");
        return;
    }

    const body =
        "studentCode="  + encodeURIComponent(studentCode) +
        "&studentName=" + encodeURIComponent(studentName) +
        "&title="       + encodeURIComponent(title) +
        "&category="    + encodeURIComponent(category) +
        "&description=" + encodeURIComponent(description);

    try {
        const r = await fetch("http://localhost:8080/addComplaint", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: body
        });
        alert(await r.text());
        document.getElementById("cTitle").value       = "";
        document.getElementById("cDescription").value = "";
        await loadMyComplaints();
    } catch(e) {
        alert("Error submitting complaint. Please check server connection.");
    }
}

async function loadMyComplaints() {
    const el   = document.getElementById("myComplaintsList");
    if (!el) return;
    const code = localStorage.getItem("studentCode") || "";

    try {
        const r    = await fetch("http://localhost:8080/studentComplaints?code=" + encodeURIComponent(code));
        const data = await r.text();
        const rows = data.trim().split("\n").filter(r => r.trim());

        if (!rows.length) {
            el.innerHTML = `<div style="text-align:center;padding:32px;color:var(--muted);">
                <div style="font-size:36px;margin-bottom:10px;">📭</div>
                <p>No complaints submitted yet.</p>
            </div>`;
            return;
        }

        el.innerHTML = rows.map(row => {
            const p = row.split(",");
            const status = p[5] || "Pending";
            const cls = status === "Resolved"    ? "badge-green"  :
                        status === "Rejected"    ? "badge-red"    :
                        status === "In Progress" ? "badge-amber"  : "badge-blue";
            return `<div class="complaint-card">
                <div style="display:flex;justify-content:space-between;align-items:flex-start;">
                    <div class="c-title">${p[1] || "Untitled"}</div>
                    <span class="badge ${cls}">${status}</span>
                </div>
                <div class="c-meta">📋 ${p[2] || ""}  &nbsp;·&nbsp;  📅 ${p[4] || ""}</div>
                <div class="c-desc">${p[3] || ""}</div>
                ${p[6] ? `<div style="margin-top:8px;font-size:12px;color:var(--muted);">
                    <strong>Admin Note:</strong> ${p[6]}
                </div>` : ""}
            </div>`;
        }).join("");

    } catch(e) {
        el.innerHTML = `<p style="color:var(--muted)">Could not load complaints.</p>`;
    }
}
async function submitLeave() {
    const studentCode = localStorage.getItem("studentCode") || "";
    const studentName = localStorage.getItem("studentName") || "";
 
    const reason      = document.getElementById("leaveReason").value;
    const destination = document.getElementById("leaveDestination").value.trim();
    const details     = document.getElementById("leaveDetails").value.trim();
    const leaveDate   = document.getElementById("leaveDate").value;
    const leaveTime   = document.getElementById("leaveTime").value;
    const returnDate  = document.getElementById("returnDate").value;
    const days        = document.getElementById("leaveDays").value;
 
    // Validation
    if (!reason) {
        showLeaveAlert("Please select a reason for leaving.", "error"); return;
    }
    if (!destination) {
        showLeaveAlert("Please enter your destination.", "error"); return;
    }
    if (!leaveDate) {
        showLeaveAlert("Please select the date of leaving.", "error"); return;
    }
    if (!leaveTime) {
        showLeaveAlert("Please select the time of leaving.", "error"); return;
    }
    if (!returnDate) {
        showLeaveAlert("Please select your expected return date.", "error"); return;
    }
    if (new Date(returnDate) < new Date(leaveDate)) {
        showLeaveAlert("Return date cannot be before the leaving date.", "error"); return;
    }
 
    const body =
        "studentCode="  + encodeURIComponent(studentCode)  +
        "&studentName=" + encodeURIComponent(studentName)  +
        "&reason="      + encodeURIComponent(reason)       +
        "&destination=" + encodeURIComponent(destination)  +
        "&details="     + encodeURIComponent(details)      +
        "&leaveDate="   + encodeURIComponent(leaveDate)    +
        "&leaveTime="   + encodeURIComponent(leaveTime)    +
        "&returnDate="  + encodeURIComponent(returnDate)   +
        "&days="        + encodeURIComponent(days);
 
    try {
        const r = await fetch("http://localhost:8080/addLeave", {
            method:  "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body:    body
        });
        const result = await r.text();
        showLeaveAlert("✅ " + result, "success");
        clearLeaveForm();
        await loadMyLeaves();
    } catch(e) {
        // Fallback: save to localStorage when server is offline
        const leaves = JSON.parse(localStorage.getItem("pendingLeaves") || "[]");
        const newLeave = {
            id: Date.now(),
            studentCode, studentName,
            reason, destination, details,
            leaveDate, leaveTime, returnDate,
            days: days || "—",
            status: "Pending",
            submittedAt: new Date().toLocaleString()
        };
        leaves.push(newLeave);
        localStorage.setItem("pendingLeaves", JSON.stringify(leaves));
        showLeaveAlert("✅ Leave request saved (will sync when server is available).", "success");
        clearLeaveForm();
        await loadMyLeaves();
    }
}
 
/**
 * Load and display the student's own leave history.
 */
async function loadMyLeaves() {
    const el   = document.getElementById("myLeavesList");
    if (!el) return;
 
    const code = localStorage.getItem("studentCode") || "";
 
    let serverLeaves = [];
    try {
        const r = await fetch(
            "http://localhost:8080/studentLeaves?code=" + encodeURIComponent(code)
        );
        const text = await r.text();
        if (text.trim()) {
            serverLeaves = text.trim().split("\n").filter(x => x.trim()).map(row => {
                // Expected CSV: id,studentCode,studentName,reason,destination,leaveDate,leaveTime,returnDate,days,status
                const p = row.split(",");
                return {
                    id:          p[0]  || "—",
                    studentCode: p[1]  || "",
                    studentName: p[2]  || "",
                    reason:      p[3]  || "—",
                    destination: p[4]  || "—",
                    leaveDate:   p[5]  || "—",
                    leaveTime:   p[6]  || "—",
                    returnDate:  p[7]  || "—",
                    days:        p[8]  || "—",
                    status:      p[9]  || "Pending",
                    details:     p[10] || ""
                };
            });
        }
    } catch(e) { /* server offline */ }
 
    // Also include any locally saved pending leaves
    const local = JSON.parse(localStorage.getItem("pendingLeaves") || "[]")
        .filter(l => l.studentCode === code);
 
    const allLeaves = [...serverLeaves, ...local];
 
    if (!allLeaves.length) {
        el.innerHTML = `<div style="text-align:center;padding:32px;color:var(--muted);">
            <div style="font-size:36px;margin-bottom:10px;">🚪</div>
            <p>No leave requests submitted yet.</p>
        </div>`;
        return;
    }
 
    el.innerHTML = `<div class="leave-timeline">` +
        allLeaves.reverse().map(l => {
            const status   = l.status || "Pending";
            const cls      = status === "Approved" ? "badge-green"  :
                             status === "Rejected" ? "badge-red"    : "badge-amber";
            return `<div class="leave-item">
                <div class="leave-status"><span class="badge ${cls}">${status}</span></div>
                <h4>📍 ${l.destination}</h4>
                <div style="font-size:13px;color:var(--muted);margin-bottom:4px;">
                    Reason: <strong style="color:var(--text);">${l.reason}</strong>
                </div>
                <div class="meta">
                    <span>🗓 Leaving: <strong>${l.leaveDate} ${l.leaveTime || ""}</strong></span>
                    <span>🗓 Return: <strong>${l.returnDate}</strong></span>
                    <span>⏱ Days Away: <strong>${l.days}</strong></span>
                    ${l.details ? `<span style="grid-column:1/-1;">📝 ${l.details}</span>` : ""}
                </div>
            </div>`;
        }).join("") +
    `</div>`;
}
 
/**
 * Show an inline alert inside the leave form area.
 */
function showLeaveAlert(msg, type) {
    const existing = document.getElementById("leaveAlert");
    if (existing) existing.remove();
 
    const colors = type === "success"
        ? "background:#d1fae5;color:#065f46;border:1px solid #a7f3d0;"
        : "background:#fee2e2;color:#991b1b;border:1px solid #fca5a5;";
 
    const div = document.createElement("div");
    div.id = "leaveAlert";
    div.style.cssText = `${colors} border-radius:8px; padding:10px 16px; font-size:13px;
        font-weight:600; margin-bottom:14px; display:flex; align-items:center; gap:8px;`;
    div.textContent = msg;
 
    const form = document.querySelector("#pg-leave .form-wrap");
    if (form) form.insertBefore(div, form.firstChild);
 
    setTimeout(() => { if (div.parentNode) div.remove(); }, 4000);
}
 

function logout() {
    localStorage.removeItem("studentName");
    localStorage.removeItem("studentCode");
    window.location.href = "login.html";
}