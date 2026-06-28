function studentSection() {
   
    showStudents();
}

// Registration / Edit form 
async function showStudentForm(prefill) {

    // Load room options
    let roomOptions = `<option value="">-- Select Room --</option>`;
    try {
        let r = await fetch("http://localhost:8080/viewRooms");
        let d = await r.text();
        for (let row of d.trim().split("\n")) {
            if (!row.trim()) continue;
            let p     = row.split(",");
            let avail = parseInt(p[1]) - parseInt(p[2]);
            let dis   = avail <= 0 ? "disabled" : "";
            let sel   = (prefill && prefill.roomNo == p[0]) ? "selected" : "";
            roomOptions += `<option value="${p[0]}" ${dis} ${sel}>
                Room ${p[0]} (Floor ${Math.floor(p[0]/100)} | ${avail} bed${avail!==1?"s":""} available)
            </option>`;
        }
    } catch(e) {
        roomOptions += `<option disabled>Could not load rooms</option>`;
    }

    let floorOptions = `<option value="">-- Select Floor --</option>`;
    for (let i = 1; i <= 5; i++) {
        let sel = (prefill && prefill.floor == i) ? "selected" : "";
        floorOptions += `<option value="${i}" ${sel}>Floor ${i}</option>`;
    }

    let isEdit = !!prefill;
    let photoPreview = (prefill && prefill.photo)
        ? `<img id="photoPreview" src="${prefill.photo}"
                style="width:100px;height:100px;object-fit:cover;
                       border-radius:50%;border:3px solid #2d5fa6;
                       display:block;margin-bottom:8px;">`
        : `<div id="photoPreview"
               style="width:100px;height:100px;border-radius:50%;
                      background:#c8d6ea;display:flex;align-items:center;
                      justify-content:center;font-size:36px;margin-bottom:8px;">
               &#128100;</div>`;

    document.getElementById("content").innerHTML =
    `<style>
        .rf{display:flex;flex-direction:column;gap:5px;margin-bottom:14px;}
        .rf label{font-size:12px;font-weight:600;color:#374151;}
        .rf label .req{color:#e53e3e;margin-left:2px;}
        .rf label .hint{font-weight:400;color:#8896a7;font-size:11px;margin-left:4px;}
        .rf input,.rf select{border:1.5px solid #d0d7e2;border-radius:6px;
            padding:9px 12px;font-size:13.5px;color:#1a2332;
            background:#fafbfd;outline:none;font-family:inherit;
            transition:border-color .15s;width:100%;max-width:100%;}
        .rf input:focus,.rf select:focus{border-color:#2d5fa6;background:#fff;}
        .rf input::placeholder{color:#b0bac8;}
        .rf input[readonly]{background:#f2f4f8;color:#6b7a8d;cursor:not-allowed;}
        .rg{display:grid;grid-template-columns:1fr 1fr;gap:0 20px;}
        .sec-lbl{font-size:11px;font-weight:700;letter-spacing:.07em;color:#2d5fa6;
            text-transform:uppercase;margin:20px 0 12px;padding-bottom:5px;
            border-bottom:1px solid #e8edf3;}
        .reg-wrap{background:#fff;border:1px solid #e0e6ed;border-radius:10px;
            padding:24px 28px;max-width:720px;}
    </style>

    <div class="reg-wrap">
        <div style="font-size:17px;font-weight:700;color:#1e2a3a;margin-bottom:4px;">
            &#128100; ${isEdit ? "Edit Student" : "Register New Student"}
        </div>
        <div style="font-size:12px;color:#7a8899;margin-bottom:16px;">
            Fields marked <span style="color:#e53e3e">*</span> are required.
        </div>

        <!-- Photo Upload -->
        <div class="sec-lbl">&#128247; Profile Photo</div>
        <div style="display:flex;align-items:center;gap:20px;margin-bottom:8px;">
            ${photoPreview}
            <div>
                <input type="file" id="photoFile" accept="image/*"
                       onchange="previewPhoto(this)"
                       style="font-size:13px;padding:6px;">
                <div style="font-size:11px;color:#7a8899;margin-top:4px;">
                    JPG / PNG / WEBP &nbsp;·&nbsp; Max 2 MB recommended
                </div>
            </div>
        </div>
        <input type="hidden" id="photoData" value="${prefill && prefill.photo ? prefill.photo : ''}">

        <!-- Personal -->
        <div class="sec-lbl">&#128100; Personal Details</div>
        <div class="rg">
            <div class="rf">
                <label>Student Name <span class="req">*</span></label>
                <input type="text" id="sname" value="${prefill ? prefill.name : ''}"
                    placeholder="e.g. Priya Sharma" maxlength="80">
            </div>
            <div class="rf">
                <label>Date of Birth <span class="req">*</span></label>
                <input type="date" id="sdob" value="${prefill ? prefill.dob : ''}"
                    max="${new Date().toISOString().split('T')[0]}">
            </div>
            <div class="rf">
                <label>Occupation <span class="req">*</span></label>
                <select id="soccupation">
                    <option value="">-- Select --</option>
                    ${["Student","Employee","Job Holder","Business Owner","Freelancer","Other"]
                        .map(o=>`<option value="${o}" ${prefill&&prefill.occupation===o?"selected":""}>${o}</option>`)
                        .join("")}
                </select>
            </div>
            <div class="rf">
                <label>Age <span class="hint">(auto from DOB)</span></label>
                <input type="number" id="sage" value="${prefill ? prefill.age : ''}"
                    placeholder="Auto-calculated" readonly>
            </div>
        </div>

        <!-- Contact -->
        <div class="sec-lbl">&#128222; Contact Information</div>
        <div class="rg">
            <div class="rf">
                <label>Email Address <span class="req">*</span></label>
                <input type="email" id="semail" value="${prefill ? prefill.email : ''}"
                    placeholder="e.g. priya@gmail.com">
            </div>
            <div class="rf">
                <label>Contact Number <span class="req">*</span></label>
                <input type="text" id="scontact" value="${prefill ? prefill.contact : ''}"
                    placeholder="e.g. 9876543210" maxlength="15">
            </div>
            <div class="rf" style="grid-column:1/-1;">
                <label>Residential Address <span class="req">*</span></label>
                <input type="text" id="saddress" value="${prefill ? prefill.address : ''}"
                    placeholder="e.g. 42 MG Road, Indore, MP – 452001">
            </div>
        </div>

        <!-- Room -->
        <div class="sec-lbl">&#127968; Room Allocation</div>
        <div class="rg">
            <div class="rf">
                <label>Room Number <span class="req">*</span></label>
                <select id="sroom" onchange="onRoomChange(this.value)">
                    ${roomOptions}
                </select>
            </div>
            <div class="rf">
                <label>Floor Number <span class="req">*</span></label>
                <select id="sfloor">
                    ${floorOptions}
                </select>
            </div>
            <div class="rf">
                <label>Hostel Joining Date <span class="req">*</span></label>
                <input type="date" id="sjoiningDate"
                    value="${prefill ? prefill.joiningDate : new Date().toISOString().split('T')[0]}">
            </div>
        </div>

        <!-- Security -->
        <div class="sec-lbl">&#128274; Account Security</div>
        <div class="rg">
            <div class="rf">
                <label>Password <span class="req">*</span></label>
                <input type="password" id="spassword" value="${prefill ? prefill.password : ''}"
                    placeholder="Min 6 characters" maxlength="50">
            </div>
        </div>

        <div style="display:flex;gap:12px;margin-top:24px;padding-top:18px;
                    border-top:1px solid #e8edf3;">
            <button onclick="${isEdit ? 'updateStudent()' : 'saveStudent()'}">
                ${isEdit ? '&#10003; Update Student' : '&#128190; Register Student'}
            </button>
            <button onclick="studentSection()">&#8592; Cancel</button>
        </div>
    </div>`;

    // Auto-calculate age from DOB
    document.getElementById("sdob").addEventListener("change", function() {
        let dob = new Date(this.value);
        if (!isNaN(dob)) {
            let today = new Date();
            let age   = today.getFullYear() - dob.getFullYear();
            let m     = today.getMonth() - dob.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) age--;
            document.getElementById("sage").value = age;
        }
    });

    if (prefill && prefill.dob) {
        document.getElementById("sdob").dispatchEvent(new Event("change"));
    }
}

// Preview selected photo and store as base64
function previewPhoto(input) {
    if (!input.files || !input.files[0]) return;
    let file = input.files[0];
    let reader = new FileReader();
    reader.onload = function(e) {
        let src = e.target.result;
        document.getElementById("photoData").value = src;
        let prev = document.getElementById("photoPreview");
        if (prev.tagName === "IMG") {
            prev.src = src;
        } else {
            prev.outerHTML = `<img id="photoPreview" src="${src}"
                style="width:100px;height:100px;object-fit:cover;
                       border-radius:50%;border:3px solid #2d5fa6;
                       display:block;margin-bottom:8px;">`;
        }
    };
    reader.readAsDataURL(file);
}

// Auto-fill floor from selected room number
function onRoomChange(roomNo) {
    if (!roomNo) return;
    let floor = Math.floor(parseInt(roomNo) / 100);
    document.getElementById("sfloor").value = floor;
}

// View all students (list) 
async function showStudents() {
    let response = await fetch("http://localhost:8080/viewStudents");
    let data     = await response.text();
    let rows     = data.trim().split("\n").filter(r => r.trim() !== "");

    let html = `
        <h3 style="margin:16px 0 10px;font-size:15px;font-weight:700;color:var(--text);">&#128100; Students List</h3>
        <div class="tbl-wrap"><table class="data-tbl">
        <thead><tr>
            <th>Photo</th><th>Code</th><th>Name</th><th>Occupation</th>
            <th>Email</th><th>Floor</th><th>Room</th><th>Joining Date</th>
            <th>Contact</th><th>Actions</th>
        </tr></thead><tbody id="studentTableBody">`;

    for (let row of rows) {
        let p = row.split(",");
        // 0=studentCode,1=id,2=name,3=age,4=roomNo,5=contact,
        // 6=occupation,7=email,8=dob,9=address,10=floor,11=joiningDate
        html += `<tr>
            <td>
                <div style="width:32px;height:32px;border-radius:50%;
                            background:#eef3fb;color:#2d5fa6;display:flex;align-items:center;
                            justify-content:center;font-size:15px;cursor:pointer;"
                     onclick='viewStudentProfile("${p[2]}")'>
                    &#128100;
                </div>
            </td>
            <td><span style="background:#eef3fb;color:#2d5fa6;padding:3px 8px;border-radius:12px;font-size:12px;font-weight:600;">${p[0]||"-"}</span></td>
            <td style="font-weight:500;cursor:pointer;color:var(--primary);"
                onclick='viewStudentProfile("${p[2]}")'>${p[2]||"-"}</td>
            <td>${p[6]||"-"}</td>
            <td>${p[7]||"-"}</td>
            <td>${p[10]||"-"}</td>
            <td style="text-align:center;">Room ${p[4]||"-"}</td>
            <td>&#128197; ${p[11]||"-"}</td>
            <td>&#128222; ${p[5]||"-"}</td>
            <td style="white-space:nowrap;">
                <button class="btn-edit" onclick='viewStudentProfile("${p[2]}")'>&#128065; View</button>
                <button class="btn-edit" onclick='editStudent("${p[2]}")'>&#9998; Edit</button>
                <button class="btn-del"  onclick='deleteStudent("${p[2]}")'>&#128465; Delete</button>
            </td>
        </tr>`;
    }
    if (rows.length === 0) html += `<tr><td colspan="10" style="text-align:center;padding:32px;color:var(--muted);">No students found.</td></tr>`;

    html += `</tbody></table></div>`;
    document.getElementById("content").innerHTML = html;
}

// Individual student profile (admin view) 
async function viewStudentProfile(name) {
    let response = await fetch(
        "http://localhost:8080/getStudent?name=" + encodeURIComponent(name)
    );
    let s = await response.json();

    let photoHtml = s.photo
        ? `<img src="${s.photo}"
                style="width:110px;height:110px;object-fit:cover;
                       border-radius:50%;border:4px solid #2d5fa6;">`
        : `<div style="width:110px;height:110px;border-radius:50%;
                       background:#c8d6ea;display:flex;align-items:center;
                       justify-content:center;font-size:42px;
                       border:4px solid #2d5fa6;">&#128100;</div>`;

    document.getElementById("content").innerHTML =
    `<div style="display:flex;align-items:center;gap:10px;margin-bottom:16px;">
        <button onclick="showStudents()">&#8592; Back to Students</button>
        <button onclick='editStudent("${s.name}")'>&#9998; Edit Profile</button>
        <button onclick='deleteStudent("${s.name}")'
                style="background:#fdecea;color:#c0392b;border-color:#f5c6c6;">
            &#128465; Delete
        </button>
    </div>

    <div style="background:#fff;border:1px solid #e0e6ed;border-radius:10px;
                overflow:hidden;max-width:680px;">

        <!-- Header with photo -->
        <div style="background:linear-gradient(135deg,#1e2a3a 0%,#2d5fa6 100%);
                    padding:24px;display:flex;align-items:center;gap:20px;">
            ${photoHtml}
            <div>
                <div style="font-size:20px;font-weight:700;color:#fff;">
                    ${s.name || "-"}
                </div>
                <div style="font-size:12px;color:#a0c4e8;margin-top:4px;">
                    ${s.studentCode || "No Code"}
                </div>
                <div style="font-size:12px;color:#c8d6e8;margin-top:2px;">
                    ${s.occupation || "-"}
                </div>
            </div>
        </div>

        <!-- Detail grid -->
        <div style="padding:20px;">
            <div style="display:grid;grid-template-columns:1fr 1fr;gap:0;">
                ${adminProfileRow("&#128231; Email",         s.email       || "-")}
                ${adminProfileRow("&#128197; Date of Birth", s.dob         || "-")}
                ${adminProfileRow("&#128202; Floor",         s.floor !== undefined ? s.floor : "-")}
                ${adminProfileRow("&#127968; Room Number",   s.roomNo      || "-")}
                ${adminProfileRow("&#128197; Joining Date",  s.joiningDate || "-")}
                ${adminProfileRow("&#128222; Contact",       s.contact     || "-")}
            </div>
            <!-- Address full width -->
            <div style="padding:10px 0;border-top:1px solid #f0f0f0;margin-top:4px;">
                <div style="font-size:11px;font-weight:700;letter-spacing:.05em;
                            color:#8896a7;text-transform:uppercase;margin-bottom:4px;">
                    &#127968; Residential Address
                </div>
                <div style="font-size:14px;color:#333;">${s.address || "-"}</div>
            </div>
        </div>
    </div>`;
}

function adminProfileRow(label, value) {
    return `
    <div style="padding:10px 0;border-bottom:1px solid #f0f0f0;">
        <div style="font-size:11px;font-weight:700;letter-spacing:.05em;
                    color:#8896a7;text-transform:uppercase;margin-bottom:3px;">
            ${label}
        </div>
        <div style="font-size:14px;color:#333;">${value}</div>
    </div>`;
}

//Save new student 
async function saveStudent() {
    let name        = document.getElementById("sname").value.trim();
    let dob         = document.getElementById("sdob").value;
    let age         = document.getElementById("sage").value;
    let occupation  = document.getElementById("soccupation").value;
    let email       = document.getElementById("semail").value.trim();
    let contact     = document.getElementById("scontact").value.trim();
    let address     = document.getElementById("saddress").value.trim();
    let roomNo      = document.getElementById("sroom").value;
    let floor       = document.getElementById("sfloor").value;
    let joiningDate = document.getElementById("sjoiningDate").value;
    let password    = document.getElementById("spassword").value;
    let photo       = document.getElementById("photoData").value;

    if (!name)        { alert("Please enter Student Name.");       return; }
    if (!dob)         { alert("Please select Date of Birth.");     return; }
    if (!occupation)  { alert("Please select Occupation.");        return; }
    if (!email)       { alert("Please enter Email Address.");      return; }
    if (!contact)     { alert("Please enter Contact Number.");     return; }
    if (!address)     { alert("Please enter Residential Address.");return; }
    if (!roomNo)      { alert("Please select Room Number.");       return; }
    if (!joiningDate) { alert("Please select Joining Date.");      return; }
    if (!password || password.length < 6) { alert("Password must be at least 6 characters."); return; }

    try {
        let response = await fetch("http://localhost:8080/addStudent", {
            method:  "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body:
                "name="         + encodeURIComponent(name)        +
                "&age="         + encodeURIComponent(age)         +
                "&roomNo="      + encodeURIComponent(roomNo)      +
                "&floor="       + encodeURIComponent(floor)       +
                "&contact="     + encodeURIComponent(contact)     +
                "&email="       + encodeURIComponent(email)       +
                "&occupation="  + encodeURIComponent(occupation)  +
                "&dob="         + encodeURIComponent(dob)         +
                "&address="     + encodeURIComponent(address)     +
                "&joiningDate=" + encodeURIComponent(joiningDate) +
                "&password="    + encodeURIComponent(password)    +
                "&photo="       + encodeURIComponent(photo)
        });

        let result = await response.text();

        if (result === "SUCCESS") {
            document.getElementById("content").innerHTML =
                `<div style="text-align:center;padding:48px 20px;">
                    <div style="font-size:48px;margin-bottom:12px;">&#10003;</div>
                    <div style="font-size:18px;font-weight:600;color:#1e2a3a;margin-bottom:6px;">
                        Student Registered Successfully
                    </div>
                    <div style="font-size:13px;color:#7a8899;margin-bottom:24px;">
                        ${name} has been added to the hostel system.
                    </div>
                    <button onclick="showStudentForm()">Register Another</button>
                    &nbsp;
                    <button onclick="showStudents()">View All Students</button>
                </div>`;
        } else {
            alert("Failed to register student. Please try again.");
        }
    } catch(err) {
        alert("Error: " + err.message);
    }
}

//Edit (load form prefilled with JSON data)
async function editStudent(name) {
    let response = await fetch(
        "http://localhost:8080/getStudent?name=" + encodeURIComponent(name)
    );
    let s = await response.json();
    await showStudentForm({
        name:        s.name        || "",
        age:         s.age         || "",
        roomNo:      s.roomNo      || "",
        contact:     s.contact     || "",
        password:    s.password    || "",
        studentCode: s.studentCode || "",
        occupation:  s.occupation  || "",
        email:       s.email       || "",
        dob:         s.dob         || "",
        address:     s.address     || "",
        floor:       s.floor       || "",
        joiningDate: s.joiningDate || "",
        photo:       s.photo       || ""
    });
}

// Update existing student
async function updateStudent() {
    let name        = document.getElementById("sname").value.trim();
    let dob         = document.getElementById("sdob").value;
    let age         = document.getElementById("sage").value;
    let occupation  = document.getElementById("soccupation").value;
    let email       = document.getElementById("semail").value.trim();
    let contact     = document.getElementById("scontact").value.trim();
    let address     = document.getElementById("saddress").value.trim();
    let roomNo      = document.getElementById("sroom").value;
    let floor       = document.getElementById("sfloor").value;
    let joiningDate = document.getElementById("sjoiningDate").value;
    let password    = document.getElementById("spassword").value;
    let photo       = document.getElementById("photoData").value;

    if (!name || !roomNo || !password) {
        alert("Please fill in all required fields.");
        return;
    }

    let response = await fetch("http://localhost:8080/updateStudent", {
        method:  "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body:
            "name="         + encodeURIComponent(name)        +
            "&age="         + encodeURIComponent(age)         +
            "&roomNo="      + encodeURIComponent(roomNo)      +
            "&floor="       + encodeURIComponent(floor)       +
            "&contact="     + encodeURIComponent(contact)     +
            "&email="       + encodeURIComponent(email)       +
            "&occupation="  + encodeURIComponent(occupation)  +
            "&dob="         + encodeURIComponent(dob)         +
            "&address="     + encodeURIComponent(address)     +
            "&joiningDate=" + encodeURIComponent(joiningDate) +
            "&password="    + encodeURIComponent(password)    +
            "&photo="       + encodeURIComponent(photo)
    });

    let result = await response.text();
    alert(result === "SUCCESS" ? "Student updated successfully." : "Update failed.");
    showStudents();
}

//Delete student
async function deleteStudent(name) {
    if (!confirm(`Delete student "${name}"? This cannot be undone.`)) return;

    let response = await fetch(
        "http://localhost:8080/deleteStudent?name=" + encodeURIComponent(name)
    );
    let result = await response.text();
    alert(result === "SUCCESS" ? "Student deleted." : "Delete failed.");
    showStudents();
}

//Employee Section
function employeeSection() {

    document.getElementById("content").innerHTML =

    `<h2>Employee Management</h2>

    <button onclick="showEmployeeForm()">
        Add Employee
    </button>

    <button onclick="showEmployees()">
        View Employees
    </button>

    <div id="employeeContent"></div>`;
}
//Add Employee
function showEmployeeForm() {

    document.getElementById("employeeContent").innerHTML =

    `<h2>Add Employee</h2><br>

    <label><b>Employee Name</b></label><br>
    <input type="text"
           id="ename"
           placeholder="Name"><br><br>
           
    <label><b>Employee role</b></label><br>

    <input type="text"
           id="erole"
           placeholder="Role"><br><br>

    <label><b>Contact Number</b></label><br>

    <input type="text"
           id="econtact"
           placeholder="Contact"><br><br>
    
    
    <button onclick="saveEmployee()">
        Save Employee
    </button>`;
}
//View Employees
async function showEmployees() {

    let response =
        await fetch(
            "http://localhost:8080/viewEmployees"
        );

    let data =
        await response.text();

    let rows =
        data.trim().split("\n");

    let html = `
        <h3 style="margin:16px 0 10px;font-size:15px;font-weight:700;color:var(--text);">&#128119; Employees List</h3>
        <div class="tbl-wrap"><table class="data-tbl">
        <thead><tr>
           <th>Name</th><th>Role</th><th>Contact</th><th>Actions</th>
        </tr></thead><tbody id="employeeTableBody">`;

    let hasEmpRows = false;
    for(let row of rows) {
        if(row.trim() === "") continue;
        hasEmpRows = true;
        let parts = row.split(",");
        html += `<tr>
          
            <td style="font-weight:500;">&#128100; ${parts[1]}</td>
            <td><span style="background:#f0fdf4;color:#166534;padding:3px 8px;border-radius:12px;font-size:12px;">${parts[2]}</span></td>
            <td>&#128222; ${parts[3]}</td>
            <td style="white-space:nowrap;">
                <button class="btn-edit" onclick="editEmployee(${parts[0]})">&#9998; Edit</button>
                <button class="btn-del"  onclick="deleteEmployee(${parts[0]})">&#128465; Delete</button>
            </td>
        </tr>`;
    }
    if (!hasEmpRows) html += `<tr><td colspan="4" style="text-align:center;padding:32px;color:var(--muted);">No employees found.</td></tr>`;
    html += `</tbody></table></div>`;

    document.getElementById("employeeContent").innerHTML = html;
}
//Save Employee
async function saveEmployee() {

    try {

        let name =
            document.getElementById("ename").value;

        let role =
            document.getElementById("erole").value;

        let contact =
            document.getElementById("econtact").value;

        let response =
            await fetch(
                "http://localhost:8080/addEmployee",
                {
                    method: "POST",

                    headers: {
                        "Content-Type":
                        "application/x-www-form-urlencoded"
                    },

                    body:
                        "name=" + encodeURIComponent(name) +
                        "&role=" + encodeURIComponent(role) +
                        "&contact=" + encodeURIComponent(contact)
                });

        let result = await response.text();

        if (result === "SUCCESS") {
            alert("Employee Added Successfully");
            showEmployees();
        } else {
            alert("Failed to Add Employee: " + result);
        }

    } catch(error) {

        alert("ERROR: " + error);
        console.log(error);
    }
}
//Edit Employee
async function editEmployee(id) {

    let response =
        await fetch(
            "http://localhost:8080/getEmployee?id=" + id
        );

    let data =
        await response.text();

    let parts =
        data.split(",");

    document.getElementById("employeeContent").innerHTML =

    `<h2>Edit Employee</h2>

    <input type="hidden"
           id="eid"
           value="${parts[0]}">

    <input type="text"
           id="ename"
           value="${parts[1]}"><br><br>

    <input type="text"
           id="erole"
           value="${parts[2]}"><br><br>

    <input type="text"
           id="econtact"
           value="${parts[3]}"><br><br>

    <button onclick="updateEmployee()">
        Update Employee
    </button>`;
}
//Delete Employee
async function deleteEmployee(id) {

    let ok =
        confirm(
            "Delete Employee?"
        );

    if(!ok) return;

    let response =
        await fetch(
            "http://localhost:8080/deleteEmployee?id="
            + id
        );

    let result =
        await response.text();

    if(result === "SUCCESS") {

        alert(
            "Employee Deleted"
        );

        showEmployees();

    } else {

        alert(
            "Delete Failed"
        );
    }
}
//Update Employee
async function updateEmployee() {

    let id =
        document.getElementById("eid").value;

    let name =
        document.getElementById("ename").value;

    let role =
        document.getElementById("erole").value;

    let contact =
        document.getElementById("econtact").value;

    let response =
        await fetch(
            "http://localhost:8080/updateEmployee",
            {
                method:"POST",

                headers:{
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "id=" + encodeURIComponent(id)
                    + "&name=" + encodeURIComponent(name)
                    + "&role=" + encodeURIComponent(role)
                    + "&contact=" + encodeURIComponent(contact)
            });

    let result =
        await response.text();

    if(result === "SUCCESS") {

        alert("Employee Updated Successfully");
        showEmployees();

    } else {

        alert("Update Failed: " + result);
    }
}
//Room Section
function roomSection() {

    document.getElementById("content").innerHTML =

    `<h2>Room Management</h2>

    <button onclick="showRoomForm()">
        Add Room
    </button>

    <button onclick="showRooms()">
        View Rooms
    </button>

    <div id="roomContent"></div>`;
}
//Show Room
function showRoomForm() {

    document.getElementById("roomContent").innerHTML =

    `<h2>Add Room</h2><br>
    <label><b>Room Number</b></label><br>
    <input type="number"
           id="roomno"
           placeholder="Room Number"><br><br>
    
    <label><b>Room Capacity</b></label><br>
    <input type="number"
           id="capacity"
           placeholder="Capacity"><br><br>
        
    <label><b>Occupied Beds</b></label><br>
    <input type="number"
           id="occupied"
           placeholder="Occupied Beds"><br><br>

    <button onclick="saveRoom()">
        Save Room
    </button>`;
}
//Save Room
async function saveRoom() {

    let roomNo =
        document.getElementById("roomno").value;

    let capacity =
        document.getElementById("capacity").value;

    let occupied =
        document.getElementById("occupied").value;

    let response =
        await fetch(
            "http://localhost:8080/addRoom",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "roomNo=" + encodeURIComponent(roomNo) +
                    "&capacity=" + encodeURIComponent(capacity) +
                    "&occupied=" + encodeURIComponent(occupied)
            });

    let result =
        await response.text();

    if(result === "SUCCESS") {

        alert("Room Added Successfully");
        showRooms();

    } else {

        alert("Failed To Add Room");
    }
}
//Show Rooms
async function showRooms() {

    let response =
        await fetch(
            "http://localhost:8080/viewRooms"
        );

    let data =
        await response.text();

    let rows =
        data.trim().split("\n");

    let html = `
        <h3 style="margin:16px 0 10px;font-size:15px;font-weight:700;color:var(--text);">&#127968; Rooms List</h3>
        <div class="tbl-wrap"><table class="data-tbl">
        <thead><tr>
            <th>Room No</th><th>Capacity</th><th>Occupied</th><th>Available</th><th>Actions</th>
        </tr></thead><tbody id="roomTableBody">`;

    let hasRoomRows = false;
    for(let row of rows) {
        if(row.trim() === "") continue;
        hasRoomRows = true;
        let parts = row.split(",");
        let avail = parseInt(parts[1]) - parseInt(parts[2]);
        let availColor = avail > 0 ? "#166534" : "#991b1b";
        let availBg    = avail > 0 ? "#f0fdf4" : "#fef2f2";
        html += `<tr>
            <td style="font-weight:700;">Room ${parts[0]}</td>
            <td style="text-align:center;">${parts[1]}</td>
            <td style="text-align:center;">${parts[2]}</td>
            <td style="text-align:center;"><span style="background:${availBg};color:${availColor};padding:3px 8px;border-radius:12px;font-size:12px;font-weight:600;">${avail}</span></td>
            <td style="white-space:nowrap;">
                <button class="btn-edit" onclick="editRoom(${parts[0]})">&#9998; Edit</button>
                <button class="btn-del"  onclick="deleteRoom(${parts[0]})">&#128465; Delete</button>
            </td>
        </tr>`;
    }
    if (!hasRoomRows) html += `<tr><td colspan="5" style="text-align:center;padding:32px;color:var(--muted);">No rooms found.</td></tr>`;
    html += `</tbody></table></div>`;

    document.getElementById("roomContent").innerHTML = html;
}
//Edit Room
async function editRoom(roomNo) {

    let response =
        await fetch(
            "http://localhost:8080/getRoom?roomNo=" + roomNo
        );

    let data =
        await response.text();

    let parts =
        data.split(",");

    document.getElementById("roomContent").innerHTML =

    `<h2>Edit Room</h2>

    <input type="number"
           id="roomno"
           value="${parts[0]}" readonly><br><br>

    <input type="number"
           id="capacity"
           value="${parts[1]}"><br><br>

    <input type="number"
           id="occupied"
           value="${parts[2]}"><br><br>

    <button onclick="updateRoom()">
        Update Room
    </button>`;
}
//Update Room
async function updateRoom() {

    let roomNo =
        document.getElementById("roomno").value;

    let capacity =
        document.getElementById("capacity").value;

    let occupied =
        document.getElementById("occupied").value;

    let response =
        await fetch(
            "http://localhost:8080/updateRoom",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "roomNo=" + encodeURIComponent(roomNo) +
                    "&capacity=" + encodeURIComponent(capacity) +
                    "&occupied=" + encodeURIComponent(occupied)
            });

    let result =
        await response.text();

    if(result === "SUCCESS") {

        alert("Room Updated Successfully");
        showRooms();

    } else {

        alert("Failed To Update Room");
    }
}
//Delete Room
async function deleteRoom(roomNo) {

    let ok =
        confirm("Delete Room?");

    if(!ok) return;

    let response =
        await fetch(
            "http://localhost:8080/deleteRoom?roomNo="
            + roomNo
        );

    let result =
        await response.text();

    alert(result);

    showRooms();
}
//Facility Section
function facilitySection() {

    document.getElementById("content").innerHTML =

    `<h2>Facility Management</h2><br>

    <button onclick="showFacilityForm()">
        Add Facility
    </button>

    <button onclick="showFacilities()">
        View Facilities
    </button>

    <div id="facilityContent"></div>`;
}
//Add Facility
function showFacilityForm() {

    document.getElementById("content").innerHTML =

    `<h2>Add Facility</h2><br>
    <label><b>  Facility Name </b></label><br>

    <input type="text"
           id="fname"
           placeholder="Facility Name"><br><br>
        
    <label><b>Timing</b></label><br>
    <input type="text"
           id="ftiming"
           placeholder="Timing"><br><br>
    
    <label><b>Description</b></label><br>
    <textarea
           id="fdescription"
           placeholder="Description">
    </textarea><br><br>

    <button onclick="saveFacility()">
        Save Facility
    </button>`;
}
//Save Facility
async function saveFacility() {

    let facilityName =
        document.getElementById("fname").value;

    let timing =
        document.getElementById("ftiming").value;

    let description =
        document.getElementById("fdescription").value;

    let response =
        await fetch(
            "http://localhost:8080/addFacility",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "facilityName=" + encodeURIComponent(facilityName) +
                    "&timing=" + encodeURIComponent(timing) +
                    "&description=" + encodeURIComponent(description)
            });

    let result =
        await response.text();

    if(result === "SUCCESS") {

        alert("Facility Added Successfully");

    } else {

        alert("Failed To Add Facility");
    }
}
//Show Facilities
async function showFacilities() {

    let response =
        await fetch(
            "http://localhost:8080/viewFacilities"
        );

    let data =
        await response.text();

    let rows =
        data.trim().split("\n").filter(r => r.trim() !== "");

    let html =
        `<h3 style="margin:16px 0 10px;font-size:15px;font-weight:700;color:var(--text);">
            🏢 Facilities List
        </h3>
        <div class="tbl-wrap">
        <table class="data-tbl">
        <thead><tr>
            <th>Facility Name</th>
            <th>Timing</th>
            <th>Description</th>
            <th>Actions</th>
        </tr></thead><tbody id="facilityTableBody">`;

    if (rows.length === 0) {
        html += `<tr><td colspan="4" style="text-align:center;padding:32px;color:var(--muted);">No facilities found.</td></tr>`;
    }

    for (let row of rows) {
        // Split on first 3 commas only — description may contain commas
        let idx0 = row.indexOf(",");
        let idx1 = row.indexOf(",", idx0 + 1);
        let idx2 = row.indexOf(",", idx1 + 1);

        let id   = row.substring(0, idx0).trim();
        let name = row.substring(idx0 + 1, idx1).trim();
        let time = row.substring(idx1 + 1, idx2).trim();
        let desc = row.substring(idx2 + 1).trim();

        html +=
            `<tr>
                <td style="font-weight:500;">${name}</td>
                <td><span style="background:#f0f4ff;color:#3b5bdb;padding:3px 8px;border-radius:12px;font-size:12px;">⏰ ${time}</span></td>
                <td style="max-width:250px;color:var(--muted);font-size:12.5px;">${desc}</td>
                <td style="white-space:nowrap;">
                    <button class="btn-edit" onclick='editFacility(${id})'>✏ Edit</button>
                    <button class="btn-del"  onclick='deleteFacility(${id})'>🗑 Delete</button>
                </td>
            </tr>`;
    }

    html += `</tbody></table></div>`;

    document.getElementById("facilityContent").innerHTML = html;
}
//Edit Facility
async function editFacility(id) {

    let response =
        await fetch(
            "http://localhost:8080/getFacility?id=" + id
        );

    let data =
        await response.text();

    let parts =
        data.split(",");

    document.getElementById("content").innerHTML =

    `<h2>Edit Facility</h2>

    <input type="hidden"
           id="fid"
           value="${parts[0]}">

    <input type="text"
           id="fname"
           value="${parts[1]}"><br><br>

    <input type="text"
           id="ftiming"
           value="${parts[2]}"><br><br>

    <textarea id="fdescription">${parts[3]}</textarea>
    <br><br>

    <button onclick="updateFacility()">
        Update Facility
    </button>`;
}
// Update Facility
async function updateFacility() {

    let id =
        document.getElementById("fid").value;

    let facilityName =
        document.getElementById("fname").value;

    let timing =
        document.getElementById("ftiming").value;

    let description =
        document.getElementById("fdescription").value;

    let response =
        await fetch(
            "http://localhost:8080/updateFacility",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "id=" + encodeURIComponent(id)
                    + "&facilityName=" + encodeURIComponent(facilityName)
                    + "&timing=" + encodeURIComponent(timing)
                    + "&description=" + encodeURIComponent(description)
            });

    let result =
        await response.text();

    alert(result);

    showFacilities();
}
//Delete Facility
async function deleteFacility(id) {

    let ok =
        confirm("Delete Facility?");

    if(!ok) return;

    let response =
        await fetch(
            "http://localhost:8080/deleteFacility?id=" + id
        );

    let result =
        await response.text();

    alert(result);

    showFacilities();
}
//Notice Section

function noticeSection() {

    document.getElementById("content").innerHTML =

    `<h2>Notice Management</h2>

    <button onclick="showNoticeForm()">
        Add Notice
    </button>

    <button onclick="showNotices()">
        View Notices
    </button>

    <div id="noticeContent"></div>`;
}
//Add Notice   

function showNoticeForm() {

    document.getElementById("noticeContent").innerHTML =

    `<h2>Add Notice</h2><br>
    <label><b>Notice Title</b></label><br>
    <input type="text"
           id="ntitle"
           placeholder="Notice Title"><br><br>
    
    <label><b>Notice Message</b></label><br>
    <textarea
           id="nmessage"
           placeholder="Notice Message">
    </textarea><br><br>

    <button onclick="saveNotice()">
        Publish Notice
    </button>`;
}
//Save Notice
async function saveNotice() {

    let title =
        document.getElementById("ntitle").value;

    let message =
        document.getElementById("nmessage").value;

    let response =
        await fetch(
            "http://localhost:8080/addNotice",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "title=" + encodeURIComponent(title) +
                    "&message=" + encodeURIComponent(message)
            });

    let result =
        await response.text();

    if(result === "SUCCESS") {

        alert("Notice Published Successfully");
        showNotices();

    } else {

        alert("Failed To Publish Notice");
    }
}
async function showNotices() {

    let response =
        await fetch(
            "http://localhost:8080/viewNotices"
        );

    let data =
        await response.text();

    let rows =
        data.trim().split("\n");

    let html = `
        <h3 style="margin:16px 0 10px;font-size:15px;font-weight:700;color:var(--text);">&#128276; Notices Board</h3>
        <div class="tbl-wrap"><table class="data-tbl">
        <thead><tr>
           <th>Title</th><th>Message</th><th>Date Posted</th><th>Actions</th>
        </tr></thead><tbody id="noticeTableBody">`;

    let hasNoticeRows = false;
    for(let row of rows) {
        if(row.trim() === "") continue;
        hasNoticeRows = true;
        // Split on first 3 commas (message may contain commas)
        let idx0 = row.indexOf(",");
        let idx1 = row.indexOf(",", idx0 + 1);
        let idx2 = row.indexOf(",", idx1 + 1);
        let id    = row.substring(0, idx0).trim();
        let title = row.substring(idx0+1, idx1).trim();
        let msg   = row.substring(idx1+1, idx2).trim();
        let date  = row.substring(idx2+1).trim();
        html += `<tr>
            <td style="font-weight:500;">&#128196; ${title}</td>
            <td style="max-width:240px;color:var(--muted);font-size:12.5px;">${msg}</td>
            <td><span style="background:#f0f4ff;color:#3b5bdb;padding:3px 8px;border-radius:12px;font-size:12px;">&#128197; ${date}</span></td>
            <td style="white-space:nowrap;">
                <button class="btn-edit" onclick="editNotice(${id})">&#9998; Edit</button>
                <button class="btn-del"  onclick="deleteNotice(${id})">&#128465; Delete</button>
            </td>
        </tr>`;
    }
    if (!hasNoticeRows) html += `<tr><td colspan="4" style="text-align:center;padding:32px;color:var(--muted);">No notices found.</td></tr>`;
    html += `</tbody></table></div>`;

    document.getElementById("noticeContent").innerHTML = html;
}
//Edit Notice
async function editNotice(id) {

    let response =
        await fetch(
            "http://localhost:8080/getNotice?id=" + id
        );

    let data =
        await response.text();

    let parts =
        data.split(",");

    document.getElementById("noticeContent").innerHTML =

    `<h2>Edit Notice</h2>

    <input type="hidden"
           id="nid"
           value="${parts[0]}">

    <input type="text"
           id="ntitle"
           value="${parts[1]}"><br><br>

    <textarea id="nmessage">${parts[2]}</textarea>
    <br><br>

    <input type="text"
           id="ndate"
           value="${parts[3]}"><br><br>

    <button onclick="updateNotice()">
        Update Notice
    </button>`;
}
//Update Notice
async function updateNotice() {

    let id =
        document.getElementById("nid").value;

    let title =
        document.getElementById("ntitle").value;

    let message =
        document.getElementById("nmessage").value;

    let datePosted =
        document.getElementById("ndate").value;

    let response =
        await fetch(
            "http://localhost:8080/updateNotice",
            {
                method:"POST",

                headers:{
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "id=" + encodeURIComponent(id)
                    + "&title=" + encodeURIComponent(title)
                    + "&message=" + encodeURIComponent(message)
                    + "&datePosted=" + encodeURIComponent(datePosted)
            });

    let result =
        await response.text();

    alert(result);

    showNotices();
}
//Delete Notice
async function deleteNotice(id) {

    let ok =
        confirm("Delete Notice?");

    if(!ok) return;

    let response =
        await fetch(
            "http://localhost:8080/deleteNotice?id=" + id
        );

    let result =
        await response.text();

    alert(result);

    showNotices();
}
//Rent Section
function rentSection() {

    document.getElementById("content").innerHTML =

    `<h2>Rent Management</h2>

    <button onclick="showRentForm()">
        Generate Rent
    </button>

    <button onclick="showRents()">
        View Rent
    </button>

    <div id="rentContent"></div>`;
}
//Show Rent Form
function showRentForm() {
    document.getElementById("rentContent").innerHTML =

`<h2>Generate Rent</h2><br>

<label><b>Student Name</b></label><br>
<input type="text"
       id="rstudent"
       placeholder="Student Name"><br><br>

<label><b>Month</b></label><br>
<input type="text"
       id="rmonth"
       placeholder="Month"><br><br>

<label><b>Room Number</b></label><br>

<input type="number"
       id="roomNo"
       placeholder="Room No"><br><br>

<label><b>Room Rent</b></label><br>
<input type="number"
       id="roomRent"
       placeholder="Room Rent"><br><br>

<label><b>Electric Bill</b></label><br>

<input type="number"
       id="electricBill"
       placeholder="Electric Bill"><br><br>

<label><b>Meter Reading</b></label><br>
<input type="text"
       id="meterReading"
       placeholder="Meter Reading"><br><br>
<label><b>Status</b></label><br>

<select id="rstatus">
    <option>Pending</option>
    <option>Paid</option>
</select><br><br>

<button onclick="saveRent()">
    Generate Rent
</button>`;
}
//Save Rent
async function saveRent() {

    let studentName =
        document.getElementById("rstudent").value;

    let month =
        document.getElementById("rmonth").value;

    let roomNo =
        document.getElementById("roomNo").value;

    let roomRent =
        parseFloat(
            document.getElementById("roomRent").value
        );

    let electricBill =
        parseFloat(
            document.getElementById("electricBill").value
        );

    let meterReading =
        document.getElementById("meterReading").value;

    let total =
        roomRent + electricBill;

    let status =
        document.getElementById("rstatus").value;
        

    let response =
        await fetch(
            "http://localhost:8080/addRent",
            {
                method: "POST",

                headers: {
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "studentName=" +
                    encodeURIComponent(studentName)

                    + "&month=" +
                    encodeURIComponent(month)

                    + "&roomNo=" +
                    encodeURIComponent(roomNo)

                    + "&roomRent=" +
                    encodeURIComponent(roomRent)

                    + "&electricBill=" +
                    encodeURIComponent(electricBill)

                    + "&meterReading=" +
                    encodeURIComponent(meterReading)

                    + "&total=" +
                    encodeURIComponent(total)

                    + "&status=" +
                    encodeURIComponent(status)
            });

    let result =
        await response.text();

    if (result === "SUCCESS") {

        alert(
            "Rent Generated Successfully"
        );

    } else {

        alert(
            "Failed To Generate Rent"
        );
    }
}
//Show Rents

async function showRents() {

    let response =
        await fetch(
            "http://localhost:8080/viewRents"
        );

    let data =
        await response.text();

    let rows =
        data.trim().split("\n");

    let html = `
        <h3 style="margin:16px 0 10px;font-size:15px;font-weight:700;color:var(--text);">&#128176; Rent Records</h3>
        <div class="tbl-wrap"><table class="data-tbl">
        <thead><tr>
            <th>Student Code</th><th>Student</th><th>Month</th>
            <th>Room No</th><th>Room Rent</th><th>Electric Bill</th>
            <th>Meter Reading</th><th>Total</th><th>Status</th><th>Actions</th>
        </tr></thead><tbody id="rentTableBody">`;

    let hasRentRows = false;
    for (let row of rows) {
        if(row.trim() === "") continue;
        hasRentRows = true;
        let parts = row.split(",");
        let status = parts[9] || "";
        let statusColor = status === "Paid" ? "#166534" : "#991b1b";
        let statusBg    = status === "Paid" ? "#f0fdf4" : "#fef2f2";
        html += `<tr>
            <td>${parts[1]}</td>
            <td style="font-weight:500;">${parts[2]}</td>
            <td>${parts[3]}</td>
            <td style="text-align:center;">Room ${parts[4]}</td>
            <td>&#8377;${parts[5]}</td>
            <td>&#8377;${parts[6]}</td>
            <td>${parts[7]}</td>
            <td style="font-weight:700;">&#8377;${parts[8]}</td>
            <td><span style="background:${statusBg};color:${statusColor};padding:3px 8px;border-radius:12px;font-size:12px;font-weight:600;">${status}</span></td>
            <td style="white-space:nowrap;">
                <button class="btn-edit" onclick="editRent(${parts[0]})">&#9998; Edit</button>
                <button class="btn-del"  onclick="deleteRent(${parts[0]})">&#128465; Delete</button>
            </td>
        </tr>`;
    }
    if (!hasRentRows) html += `<tr><td colspan="10" style="text-align:center;padding:32px;color:var(--muted);">No rent records found.</td></tr>`;
    html += `</tbody></table></div>`;

    document.getElementById("rentContent").innerHTML = html;
}
//Edit Rent
async function editRent(id) {

    let response =
        await fetch(
            "http://localhost:8080/getRent?id=" + id
        );

    let data =
        await response.text();

    let parts =
        data.split(",");

    document.getElementById("rentContent").innerHTML =

    `<h2>Edit Rent</h2>

    <input type="hidden"
           id="rid"
           value="${parts[0]}">

    <label><b>Student Name</b></label><br>
    <input type="text"
           id="rstudent"
           value="${parts[1]}"><br><br>

    <label><b>Month</b></label><br>
    <input type="text"
           id="rmonth"
           value="${parts[2]}"><br><br>

    <label><b>Room Rent</b></label><br>
    <input type="number"
           id="roomRent"
           value="${parts[3]}"><br><br>

    <label><b>Electric Bill</b></label><br>
    <input type="number"
           id="electricBill"
           value="${parts[4]}"><br><br>

    <label><b>Meter Reading</b></label><br>
    <input type="text"
           id="meterReading"
           value="${parts[5]}"><br><br>

    <label><b>Total</b></label><br>
    <input type="number"
           id="rtotal"
           value="${parts[6]}" readonly><br><br>

    <label><b>Status</b></label><br>
    <select id="rstatus">
        <option value="Pending"  ${parts[7]==="Pending" ?"selected":""}>Pending</option>
        <option value="Paid"     ${parts[7]==="Paid"    ?"selected":""}>Paid</option>
    </select><br><br>

    <button onclick="updateRent()">
        Update Rent
    </button>`;
}
//Update Rent
async function updateRent() {

    let id =
        document.getElementById("rid").value;

    let studentName =
        document.getElementById("rstudent").value;

    let month =
        document.getElementById("rmonth").value;

    let roomRent =
        parseFloat(document.getElementById("roomRent").value) || 0;

    let electricBill =
        parseFloat(document.getElementById("electricBill").value) || 0;

    let amount = roomRent + electricBill;

    let status =
        document.getElementById("rstatus").value;

    let response =
        await fetch(
            "http://localhost:8080/updateRent",
            {
                method:"POST",

                headers:{
                    "Content-Type":
                    "application/x-www-form-urlencoded"
                },

                body:
                    "id=" + encodeURIComponent(id)
                    + "&studentName=" + encodeURIComponent(studentName)
                    + "&month=" + encodeURIComponent(month)
                    + "&amount=" + encodeURIComponent(amount)
                    + "&status=" + encodeURIComponent(status)
            });

    let result =
        await response.text();

    alert(result);

    showRents();
}
//Delete Rent
async function deleteRent(id) {

    let ok =
        confirm("Delete Rent Record?");

    if(!ok) return;

    let response =
        await fetch(
            "http://localhost:8080/deleteRent?id=" + id
        );

    let result =
        await response.text();

    alert(result);

    showRents();
}
//Complaint Management
function complaintSection() {
 
    document.getElementById("content").innerHTML =
 
    `<h2>Complaint Management</h2>
 
    <button onclick="showAllComplaints()">📋 View All Complaints</button>
 
    <div id="complaintContent"></div>`;
 
    showAllComplaints();
}
 
// Load & display all complaints
async function showAllComplaints() {
 
    let response =
        await fetch("http://localhost:8080/viewComplaints");
 
    let data = await response.text();
 
    let rows = data.trim().split("\n").filter(r => r.trim() !== "");
 
    let html = `
        <h3 style="margin:16px 0 10px;font-size:15px;font-weight:700;color:var(--text);">&#128203; All Complaints</h3>
        <div class="tbl-wrap"><table class="data-tbl">
        <thead><tr>
           <th>Student Code</th><th>Student Name</th><th>Title</th>
            <th>Category</th><th>Description</th><th>Date</th><th>Status</th><th>Remarks</th><th>Actions</th>
        </tr></thead><tbody id="complaintTableBody">`;

    for (let row of rows) {
        let p = row.split(",");
        let status = p[7] || "";
        let statusColor = status === "Resolved"    ? "#166534" :
                          status === "Rejected"    ? "#991b1b" :
                          status === "In Progress" ? "#92400e" : "#1e3a5f";
        let statusBg    = status === "Resolved"    ? "#f0fdf4" :
                          status === "Rejected"    ? "#fef2f2" :
                          status === "In Progress" ? "#fffbeb" : "#eff6ff";
        html += `<tr>
            <td>${p[1]}</td>
            <td style="font-weight:500;">${p[2]}</td>
            <td>${p[3]}</td>
            <td><span style="background:#f5f3ff;color:#5b21b6;padding:3px 8px;border-radius:12px;font-size:12px;">${p[4]}</span></td>
            <td style="max-width:200px;color:var(--muted);font-size:12.5px;">${p[5]}</td>
            <td style="font-size:12px;">&#128197; ${p[6]}</td>
            <td><span style="background:${statusBg};color:${statusColor};padding:3px 8px;border-radius:12px;font-size:12px;font-weight:600;">${status}</span></td>
            <td style="font-size:12.5px;color:var(--muted);">${p[8] || "—"}</td>
            <td style="white-space:nowrap;">
                <button class="btn-edit" onclick="editComplaint(${p[0]},\'${status}\',\'${(p[8]||'').replace(/\'/g,"\\'")}\')">&#9998; Update</button>
                <button class="btn-del"  onclick="removeComplaint(${p[0]})">&#128465; Delete</button>
            </td>
        </tr>`;
    }

    if (rows.length === 0) {
        html += `<tr><td colspan="9" style="text-align:center;padding:32px;color:var(--muted);">No complaints found.</td></tr>`;
    }
    html += `</tbody></table></div>`;

    document.getElementById("complaintContent").innerHTML = html;
    document.getElementById("complaintContent").innerHTML = html;
}
 
//  Edit status / remarks form
function editComplaint(id, currentStatus, currentRemarks) {
 
    document.getElementById("complaintContent").innerHTML =
 
    `<h3 style="margin:16px 0 10px;">Update Complaint #${id}</h3>
 
    <label>Status:</label><br>
    <select id="cStatus" style="margin-bottom:12px;padding:6px;width:220px;">
        <option value="Pending"     ${currentStatus==='Pending'     ?'selected':''}>Pending</option>
        <option value="In Progress" ${currentStatus==='In Progress' ?'selected':''}>In Progress</option>
        <option value="Resolved"    ${currentStatus==='Resolved'    ?'selected':''}>Resolved</option>
        <option value="Rejected"    ${currentStatus==='Rejected'    ?'selected':''}>Rejected</option>
    </select><br>
 
    <label>Remarks / Admin Response:</label><br>
    <textarea id="cRemarks"
              rows="4"
              style="width:100%;max-width:500px;margin-bottom:12px;padding:8px;"
              placeholder="Write your response or action taken...">${currentRemarks}</textarea><br>
 
    <button onclick="saveComplaintUpdate(${id})">💾 Save Update</button>
    <button onclick="showAllComplaints()" style="margin-left:8px;">✖ Cancel</button>`;
}
 
//Save status update
async function saveComplaintUpdate(id) {
 
    let status  = document.getElementById("cStatus").value;
    let remarks = document.getElementById("cRemarks").value.trim();
 
    let body =
        "id=" + id +
        "&status=" + encodeURIComponent(status) +
        "&remarks=" + encodeURIComponent(remarks);
 
    let response =
        await fetch("http://localhost:8080/updateComplaint", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: body
        });
 
    let result = await response.text();
    alert(result);
    showAllComplaints();
}
 
//  Delete complaint
async function removeComplaint(id) {
 
    if (!confirm("Delete complaint #" + id + "?")) return;
 
    let response =
        await fetch("http://localhost:8080/deleteComplaint", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: "id=" + id
        });
 
    let result = await response.text();
    alert(result);
    showAllComplaints();
}
// Alias: admin.html calls showComplaints()
function showComplaints() {
    showAllComplaints();
}
// Load leave requests
async function loadAdminLeaves() {
    const el = document.getElementById("leaveAdminContent");
    if (!el) return;
 
    el.innerHTML = `<p style="color:var(--muted);font-size:13px;">Loading leave requests…</p>`;
 
    let leaves = [];
 
    try {
        const r    = await fetch("http://localhost:8080/viewLeaves");
        const text = await r.text();
        if (text.trim()) {
            leaves = text.trim().split("\n").filter(x => x.trim()).map(row => {
                // CSV: id,studentCode,studentName,reason,destination,leaveDate,leaveTime,returnDate,days,status,details
                const p = row.split(",");
                return {
                    id:          p[0]  || "—",
                    studentCode: p[1]  || "",
                    studentName: p[2]  || "—",
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
    } catch(e) {
        el.innerHTML = `<div style="text-align:center;padding:32px;color:var(--muted);">
            <div style="font-size:36px;">⚠️</div>
            <p style="margin-top:8px;">Could not connect to server.<br>
            Make sure the hostel server is running on port 8080.</p>
        </div>`;
        return;
    }
 
    // Badge for pending count
    const pending = leaves.filter(l => l.status === "Pending").length;
    const badge   = document.getElementById("leavePendingBadge");
    if (badge) {
        badge.innerHTML = pending > 0
            ? `<span class="badge badge-amber">⏳ ${pending} Pending</span>`
            : `<span class="badge badge-green">✅ All Reviewed</span>`;
    }
 
    if (!leaves.length) {
        el.innerHTML = `<div style="text-align:center;padding:48px;color:var(--muted);">
            <div style="font-size:40px;margin-bottom:12px;">🚪</div>
            <p>No leave requests submitted yet.</p>
        </div>`;
        return;
    }
 
    el.innerHTML = `
        <div class="tbl-wrap">
            <table class="data-tbl">
                <thead>
                    <tr>
                        <th>Student</th>
                        <th>Reason</th>
                        <th>Destination</th>
                        <th>Leaving</th>
                        <th>Return</th>
                        <th>Days</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="leaveTableBody">
                    ${leaves.map((l, i) => {
                        const cls = l.status === "Approved" ? "badge-green"  :
                                    l.status === "Rejected" ? "badge-red"    : "badge-amber";
                        return `<tr>
                           
                            <td>
                                <strong>${l.studentName}</strong>
                                <div style="font-size:11px;color:var(--muted);">${l.studentCode}</div>
                            </td>
                            <td>${l.reason}</td>
                            <td>📍 ${l.destination}${l.details ? `<div style="font-size:11px;color:var(--muted);">${l.details}</div>` : ""}</td>
                            <td>
                                <div>${l.leaveDate}</div>
                                <div style="font-size:11px;color:var(--muted);">⏰ ${l.leaveTime}</div>
                            </td>
                            <td>${l.returnDate}</td>
                            <td><strong>${l.days}</strong> day${l.days != 1 ? "s" : ""}</td>
                            <td><span class="badge ${cls}">${l.status}</span></td>
                            <td>
                                ${l.status === "Pending" ? `
                                    <button class="btn btn-success btn-sm" onclick="updateLeaveStatus('${l.id}', 'Approved')">✓ Approve</button>
                                    <button class="btn btn-danger btn-sm" onclick="updateLeaveStatus('${l.id}', 'Rejected')" style="margin-top:4px;">✕ Reject</button>
                                ` : `<span style="color:var(--muted);font-size:12px;">${l.status}</span>`}
                            </td>
                        </tr>`;
                    }).join("")}
                </tbody>
            </table>
        </div>`;
}
// Update leave status
async function updateLeaveStatus(leaveId, status) {
    if (!confirm(`${status === "Approved" ? "Approve" : "Reject"} this leave request?`)) return;
 
    const body = "id=" + encodeURIComponent(leaveId) +
                 "&status=" + encodeURIComponent(status);
 
    try {
        const r = await fetch("http://localhost:8080/updateLeaveStatus", {
            method:  "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body:    body
        });
        const result = await r.text();
        alert("✅ " + result);
        await loadAdminLeaves(); // Refresh table
    } catch(e) {
        alert("Error updating leave status. Please check server connection.");
    }
}
function logout() {
    localStorage.removeItem("AdminName");
    window.location.href = "login.html";
}
// Filter table
function filterTable(tbodyId, query) {
    const tbody = document.getElementById(tbodyId);
    if (!tbody) return;

    const term = query.trim().toLowerCase();
    const rows = tbody.querySelectorAll("tr");
    let visibleCount = 0;

    rows.forEach(row => {
        const text = row.textContent.toLowerCase();
        const matches = text.includes(term);
        row.style.display = matches ? "" : "none";
        if (matches) visibleCount++;
    });

    // Show a "no results" message when nothing matches a non-empty search
    let noResultsRow = tbody.querySelector("tr.no-results-row");
    if (term && visibleCount === 0) {
        if (!noResultsRow) {
            const colCount = tbody.parentElement.querySelectorAll("thead th").length || 1;
            noResultsRow = document.createElement("tr");
            noResultsRow.className = "no-results-row";
            noResultsRow.innerHTML =
                `<td colspan="${colCount}" style="text-align:center;padding:32px;color:var(--muted);">No matching results found.</td>`;
            tbody.appendChild(noResultsRow);
        }
        noResultsRow.style.display = "";
    } else if (noResultsRow) {
        noResultsRow.style.display = "none";
    }
}