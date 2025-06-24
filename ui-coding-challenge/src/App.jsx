import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import UserList from "./components/UserList";
import AddUser from "./components/AddUser";
import EditUser from "./components/EditUser";

function App(){
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/users" element={<UserList />} />
        <Route path="/users/new" element={<AddUser />} />
        <Route path="/users/:id" element={<EditUser />} />
      </Routes>
    
    </BrowserRouter>

  )
}
export default App;