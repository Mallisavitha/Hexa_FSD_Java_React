import { Link } from "react-router-dom";
import '../../css/doctor.css';

function Sidebar() {
    
    return (
        <aside className="sidebar">
            <p className="sidebar-header">AmazeCare</p>

            <Link to="/patient" className="nav-link">
                <i class="fa-solid fa-house"></i>
                Book Appointment
            </Link>

            <Link to="appointment/my" className="nav-link">
                <i class="fa-solid fa-calendar"></i>
                My Appointment
            </Link>

            <Link to="appointment/completed" className="nav-link">
                <i class="fa-solid fa-calendar"></i>
                Past Appointment
            </Link>

            <Link to="profile" className="nav-link">
                <i className="fa-solid fa-person" aria-hidden="true"></i>
                <span> Patient Profile</span>
            </Link>
            
        </aside>
    );
}

export default Sidebar;
