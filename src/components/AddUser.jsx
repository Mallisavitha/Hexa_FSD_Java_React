import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function AddUser() {
    const [form, setForm] = useState({ name: '', email: '', gender: 'male', status: 'active' });
    const navigate = useNavigate();
    const token = '185c7d65c4825310efe75d71222d16a039ffbecc5fc5066cce15185e5398bedf';

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post(`https://gorest.co.in/public/v2/users`, form, {
                headers: { Authorization: `Bearer ${token}` },
            });
            alert("User added successfully");
            navigate("/users");
        } catch (err) {
            alert("Error adding user: " + err.response?.data?.message || err.message);
        }
    };

    return (
        <div className="container-fluid" style={{ display: "flex", justifyContent: "center", paddingTop: "80px" }}>
            <div className="container" style={{ width: "500px",marginLeft: "300px",padding: "30px",border: "1px solid #ccc",borderRadius: "8px",}}>
                <h2 style={{ textAlign: "center", marginBottom: "10px" }}>Add New User</h2>
                <form onSubmit={handleSubmit}>
                    <div style={{ marginBottom: "15px" }}>
                        <label>Name:</label><br />
                        <input type="text" name="name" value={form.name} onChange={handleChange} required style={{ width: "90%", padding: "10px", fontSize: "16px" }}/>
                    </div>
                    <div style={{ marginBottom: "15px" }}>
                        <label>Email:</label><br />
                        <input type="email" name="email" value={form.email} onChange={handleChange} required style={{ width: "90%", padding: "10px", fontSize: "16px" }}/>
                    </div>
                    <div style={{ marginBottom: "15px" }}>
                        <label>Gender:</label><br />
                        <select name="gender" value={form.gender} onChange={handleChange} style={{ width: "95%", padding: "10px", fontSize: "16px" }}>
                            <option value="male">Male</option>
                            <option value="female">Female</option>
                        </select>
                    </div>
                    <div style={{ marginBottom: "25px" }}>
                        <label>Status:</label><br />
                        <select name="status" value={form.status} onChange={handleChange} style={{ width: "95%", padding: "10px", fontSize: "16px" }}>
                            <option value="active">Active</option>
                            <option value="inactive">Inactive</option>
                        </select>
                    </div>
                    <button type="submit"
                        style={{ width: "100%",padding: "12px",fontSize: "16px",backgroundColor: "#007bff",color: "#fff",border: "none",borderRadius: "4px",cursor: "pointer"}}>
                        Add User
                    </button>
                </form>
            </div>
        </div>
    );
}

export default AddUser;
