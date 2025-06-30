import axios, { AxiosHeaders } from "axios";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { setUserDetails } from "../store/actions/UserAction";
import '../css/doctor.css'

function Login() {
    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");
    let [msg, setMsg] = useState("");
    const navigate = useNavigate();
    const dispatch = useDispatch();

    const processLogin = async () => {
        // Encode username and password using btoa 
        let encodedString = window.btoa(username + ':' + password);
        //console.log(encodedString)
        //console.log(window.atob(encodedString))
        try {
            const response = await axios.get('http://localhost:8080/api/user/token', {
                headers: { "Authorization": "Basic " + encodedString }
            })
            //console.log(response.data.token)
            let token = response.data.token; //<-- this is our access token, save it for later usage. (redux,localstorage)
            localStorage.setItem('token', token); //<-- saving token for future use in browsers local storage mem
            // Step 2: Get User Details 
            let details = await axios.get('http://localhost:8080/api/user/details', {
                headers: { "Authorization": "Bearer " + token }
            }
            )
            let user={
                'username': username,
                'role': details.data.user.role
            }
            setUserDetails(dispatch)(user);
            //console.log(details)

            let name = details.data.name;
            localStorage.setItem('name', name);
            let role = details.data.user.role;
            switch (role) {
                case "PATIENT":
                    navigate("/patient")
                    break;
                case "DOCTOR":
                    navigate("/doctor")
                    break;
                case "RECEPTIONIST":
                    navigate("/receptionist")
                    break;
                case "LABSTAFF":
                    navigate("/labstaff")
                    break;
                default:
                    setMsg("Login Disabled, Contact Admin at admin@example.com")
            }
            setMsg("Login Success!!!")
        }
        catch (err) {
            setMsg('Invalid Credentials')
        }


    }
    return (
    <div className="container" style={{ marginLeft: "100%" , width:"400px",marginBottom:"20px"}} >
        <div className="card shadow" >
            <div className="form-body"><br/>
                <h2 className="div" style={{textAlign:"center"}}>Login</h2>
                {msg !== "" && (
                    <div className="alert alert-info">
                        {msg}
                    </div>
                )}

                <div className="mb-3" style={{marginLeft:"10%",marginRight:"10%"}}>
                    <label htmlFor="username">Enter username:</label>
                    <input
                        type="text"
                        id="username"
                        className="form-control"
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div className="mb-3" style={{marginLeft:"10%",marginRight:"10%"}}>
                    <label htmlFor="password">Enter password:</label>
                    <input
                        type="password"
                        id="password"
                        className="form-control"
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                <div className="text-center">
                    <button className="btn btn-primary" onClick={processLogin}>
                        Login
                    </button>
                </div><br/>
                <div className="div"style={{marginLeft:"10%",marginRight:"10%"}}>
                Don't have an account? <Link to="/signup"> Sign up here.</Link>
                </div>
            </div>
        </div>
    </div>
);


}

export default Login; 