import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./components/Login";
import DoctorDashboard from "./components/doctor/DoctorDashboard";
import Consultation from "./components/doctor/Consultation";
import ManageSlot from "./components/doctor/ManageSlot";
import MyProfile from "./components/doctor/MyProfile";
import Dashboard from "./components/doctor/Dashboard";
import NewAppointments from "./components/doctor/NewAppointments";
import UpcomingAppointments from "./components/doctor/UpcomingAppointments";
import PastAppointments from "./components/doctor/PastAppointments";
import ReceptionsitDashboard from "./components/receptionist/ReceptionistDashboard";
import AdminDashboard from "./components/receptionist/AdminDashboard";
import DoctorDetails from "./components/receptionist/DoctorDetails";
import Profile from "./components/receptionist/Profile";
import SignUp from "./components/SignUp";
import BookAppointment from "./components/patient/BookAppointment";
import PatientDashboard from "./components/patient/PatientDashboard";
import MyAppointment from "./components/patient/MyAppointment";
import PatientProfile from "./components/patient/PatientProfile";
import Past from "./components/patient/Past";
import ConsultationDetail from "./components/patient/ConsultationDetail";
import UploadTest from "./components/labstaff/UploadTest";
import LabStaffDashboard from "./components/labstaff/LabStaffDashboard";
import StaffProfile from "./components/labstaff/StaffProfile";

function App(){

  return(
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Login />}></Route>
        <Route path="/signup" element={<SignUp />}></Route>

        <Route path="/doctor" element={<DoctorDashboard />}>
          <Route index element={<Dashboard />}/>
          <Route path="appointments/new" element={<NewAppointments />} />
          <Route path="appointments/upcoming" element={<UpcomingAppointments />} />
          <Route path="appointments/past" element={<PastAppointments />} />
          <Route path="slots" element={<ManageSlot />} />
          <Route path="profile" element={<MyProfile />} />
          <Route path="consultation/:cid" element={<Consultation />} />
        </Route>

        <Route path="/receptionist" element={<ReceptionsitDashboard />} >
          <Route index element={<AdminDashboard />} />
          <Route path="doctor" element={<DoctorDetails />} />
          <Route path="profile" element={<Profile />} />
        </Route>

        <Route path="/patient" element={<PatientDashboard/>}>
          <Route index element={<BookAppointment />} />
          <Route path="appointment/my" element={<MyAppointment/>}/>
          <Route path="appointment/completed" element={<Past/>} />
          <Route path="profile" element={<PatientProfile />} />
          <Route path="consultation/:cid" element={<ConsultationDetail />} />
        </Route>

        <Route path="/labstaff" element={<LabStaffDashboard/>}>
          <Route index element={<UploadTest/>}/>
          <Route path="profile" element={<StaffProfile />} />
        </Route>
      </Routes>

    
    </BrowserRouter>
  )
}

export default App;