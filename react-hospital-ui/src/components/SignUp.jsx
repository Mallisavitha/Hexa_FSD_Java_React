import React, { useState } from "react";
import axios from "axios";
import { useNavigate, Link } from "react-router-dom";
import "../css/doctor.css"

function Signup() {
    const [formData, setFormData] = useState({
        fullName: "",
        dob: "",
        gender: "",
        contactNumber: "",user:{
        username: "",
        password: ""
        }
    });

    const [msg, setMsg] = useState("");
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "username" || name === "password") {
            setFormData({ ...formData, user: { ...formData.user, [name]: value } });
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };


    const handleSubmit = async () => {
        try {
            const res = await axios.post("http://localhost:8080/api/patient/add", formData);
            setMsg(res.data);
            setMsg("SignUp successful. Please Login")
            navigate("/");
        } catch (err) {
            setMsg("Signup failed");
            cosole.log("signup failed:"+err);
        }
    };

    return (
        <div className="container justify-content-center align-items-center" style={{ width: "500px",minHeight:"500px",marginLeft:"400px" }}>
            <div className="card shadow ">
                <div className="form-body"  style={{minHeight:"500px"}}>
                <h3 className="text-center mt-5 mb-3">Patient Signup</h3>
                {msg && <div className="alert alert-info">{msg}</div>}
                <input className="form-control mb-3"  style={{ width: "80%", margin: "0 auto" }} type="text" name="fullName" placeholder="Full Name" onChange={handleChange} />
                <input className="form-control mb-3" style={{ width: "80%", margin: "0 auto" }} type="date" name="dob" placeholder="DOB" onChange={handleChange} />
                <select className="form-control mb-3" style={{ width: "80%", margin: "0 auto" }} name="gender" onChange={handleChange}>
                    <option value="">Select Gender</option>
                    <option value="MALE">Male</option>
                    <option value="FEMALE">Female</option>
                </select>
                <input className="form-control mb-3" style={{ width: "80%", margin: "0 auto" }} type="text" name="contactNumber" placeholder="Contact Number" onChange={handleChange} />
                <input className="form-control mb-3" style={{ width: "80%", margin: "0 auto" }} type="text" name="username" placeholder="Username" onChange={handleChange} />
                <input className="form-control mb-3" style={{ width: "80%", margin: "0 auto" }} type="password" name="password" placeholder="Password" onChange={handleChange} />
                <button className="btn btn-primary" style={{ marginRight: "80px", marginLeft: "205px", }} onClick={handleSubmit}>Sign Up</button>
                <p className="text-center mt-3">
                    Already have an account? <Link to="/">Login</Link>
                </p>
            </div>
            </div>
        </div>
    );
}

export default Signup;
