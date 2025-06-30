import { Link } from "react-router-dom";
import '../../css/doctor.css';

function Sidebar() {
    
    return (
        <aside className="sidebar">
            <p className="sidebar-header">AmazeCare</p>

            <Link to="/receptionist" className="nav-link">
                <i class="fa-solid fa-house"></i>
                Dashboard
            </Link>

            <Link to="doctor" className="nav-link">
                <i class="fa-solid fa-stethoscope"></i>
                Doctor Details
            </Link>

            <Link to="profile" className="nav-link">
                <i className="fa-solid fa-user-doctor" aria-hidden="true"></i>
                <span> My Profile</span>
            </Link>
            
        </aside>
    );
}

export default Sidebar;
