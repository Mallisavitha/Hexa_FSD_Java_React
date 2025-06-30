import axios from "axios";

//  Fetch Slots
export const fetchDoctorSlots = () => async (dispatch) => {
    try {
        const token = localStorage.getItem("token");
        const res = await axios.get(`http://localhost:8080/api/doctor-slot/my-slot`, {
            headers: { Authorization: `Bearer ${token}` },
        });
        dispatch({
            type: "SET_DOCTOR_SLOTS",
            payload: res.data,
        });
    } catch (err) {
        console.error("Fetch error", err);
    }
};

//  Add Slot
export const addDoctorSlot = (slot) => async (dispatch) => {
    try {
        const token = localStorage.getItem("token");
        await axios.post(`http://localhost:8080/api/doctor-slot/add`, {
            ...slot,
            maxAppointment: parseInt(slot.maxAppointment),
        }, {
            headers: { Authorization: `Bearer ${token}` },
        });

        // Re-fetch slots after adding
        dispatch(fetchDoctorSlots());
    } catch (err) {
        console.error("Add error", err);
    }
};

// Delete Slot
export const deleteDoctorSlot = (slotId) => async (dispatch) => {
    try {
        const token = localStorage.getItem("token");
        await axios.delete(`http://localhost:8080/api/doctor-slot/delete/${slotId}`, {
            headers: { Authorization: `Bearer ${token}` },
        });

        dispatch({
            type: "DELETE_DOCTOR_SLOT",
            payload: slotId,
        });
    } catch (err) {
        console.error("Delete error", err);
    }
};
