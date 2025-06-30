import { useState } from "react"
import { useNavigate } from "react-router-dom";
import '../../css/doctor.css'
import { useDispatch, useSelector } from "react-redux";
import { setUserDetails } from "../../store/actions/UserAction";

function Navbar(){
    const[user,] =useState(useSelector(state => state.user));
    const navigate = useNavigate();
    const dispatch = useDispatch();
    const logout =() => {
        let u = {
            "username": "",
            "role":""
        }
        setUserDetails(dispatch)(u);
        localStorage.clear();
        navigate("/")
    }

    return(
        <div className="navbar-header"style={{marginLeft:"20%"}}>
            <button className="navbar-logout" onClick={logout}>Logout</button>
        </div>
    )
    
}

export default Navbar;