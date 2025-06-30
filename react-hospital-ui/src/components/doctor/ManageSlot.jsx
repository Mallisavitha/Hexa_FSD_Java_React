import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { addDoctorSlot, deleteDoctorSlot, fetchDoctorSlots } from "../../store/actions/DoctorSlotAction";

function ManageSlot() {
    const dispatch = useDispatch();//send actions to redux
    const slots = useSelector((state) => state.doctorSlot.slots);//reads slots from redux

    const [form, setForm] = useState({
        date: "",
        time: "",
        maxAppointment: ""
    });

    // Fetch slots on component mount
    useEffect(() => {
        dispatch(fetchDoctorSlots());
    }, [dispatch]);

    // Add slot
    const handleAddSlot = () => {
        if (!form.date || !form.time || !form.maxAppointment) {
            alert("Please fill all fields");
            return;
        }

        alert("Slot added");
        dispatch(addDoctorSlot(form));
        setForm({ date: "", time: "", maxAppointment: "" });
        
    };

    // Delete slot
    const handleDelete = (slotId) => {
        if (window.confirm("Are you sure you want to delete this slot?")) {
            dispatch(deleteDoctorSlot(slotId));
            alert("Slot deleted");
        }
        
    };

    return (
        <div className="container" style={{ maxWidth: "700px", marginTop: "10%", marginLeft: "30%" }}>
            {/* Add Slot Section */}
            <div className="card shadow-sm mb-4">
                <div className="card-body text-center">
                    <h4 className="mb-3">Manage My Slots</h4>
                    <div className="d-flex flex-wrap justify-content-center gap-3">
                        <input type="date" className="form-control" style={{ maxWidth: "200px" }} value={form.date} onChange={(e) => setForm({ ...form, date: e.target.value })}/>
                        <input type="time" className="form-control" style={{ maxWidth: "150px" }} value={form.time} onChange={(e) => setForm({ ...form, time: e.target.value })}/>
                        <input type="number" className="form-control" style={{ maxWidth: "150px" }} placeholder="Max App" value={form.maxAppointment} onChange={(e) => setForm({ ...form, maxAppointment: e.target.value })}/>
                        <button className="btn btn-success" onClick={handleAddSlot}>
                            Add Slot
                        </button>
                    </div>
                </div>
            </div>

            {/* Slot Table */}
            <div className="card shadow-sm">
                <div className="card-body">
                    <h5 className="mb-3 text-center">Your Available Slots</h5>
                    <div className="table-responsive">
                        <table className="table table-bordered text-center mb-0">
                            <thead className="table-light">
                                <tr>
                                    <th>Date</th>
                                    <th>Time</th>
                                    <th>Max Appointments</th>
                                    <th>Booked</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {slots.length === 0 ? (
                                    <tr>
                                        <td colSpan="5">No slots found.</td>
                                    </tr>
                                ) : (
                                    slots.map((slot) => (
                                        <tr key={slot.slotId}>
                                            <td>{slot.date}</td>
                                            <td>{slot.time}</td>
                                            <td>{slot.maxAppointment}</td>
                                            <td>{slot.bookedCount}</td>
                                            <td>
                                                <button className="btn btn-sm btn-danger" onClick={() => handleDelete(slot.slotId)}>
                                                    Delete
                                                </button>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ManageSlot;
