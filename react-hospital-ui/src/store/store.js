//I vl configure store here
// src/store/store.js

import { configureStore } from "@reduxjs/toolkit";
import UserReducer from "./reducers/UserReducer";
import DoctorSlotReducer from "./reducers/DoctorSlotReducer";

//Register all your reducers
const store= configureStore({
    reducer:{
        user: UserReducer, // come back here later aftercreating reducer
        doctorSlot: DoctorSlotReducer
    }
})

export default store;