const initialState = {
    slots:[],
};

const DoctorSlotReducer = (state =initialState, action) =>{
    switch(action.type){
        case "SET_DOCTOR_SLOTS":
            return{
                ...state,
                slots: action.payload,
            };

        case "ADD_DOCTOR_SLOT":
            return{
                ...state,
                slots:[...state.slots, action.payload],
            };

        case "DELETE_DOCTOR_SLOT":
            return{
                ...state,
                slots: state.slots.filter(slot=> slot.slotId !== action.payload),
            };

        default:
            return state;
    }
}

export default DoctorSlotReducer;