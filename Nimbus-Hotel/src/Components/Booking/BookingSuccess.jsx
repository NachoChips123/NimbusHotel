import React from "react";
import { useLocation } from "react-router-dom";
import Header from "../Common/Header";


const BookingSuccess = () => {

    const location = useLocation()
    const message = location.state?.message
    const error = location.state?.error
    return(
        <div className="container">
            <Header title="Booking Success"/>
            <div className="mt-5">
                {message ? (
                    <div>
                        <h3 className="text-success"> Booking Successful!</h3>
                        <p className="text-success">{message}</p>   
                    </div>
                ): (
                    <div>
                        <h3 className="text-danger"> There was an Error in Booking the Room</h3>
                        <p className="text-danger">{error}</p>
                    </div>
                )}
            </div>
        </div>
    )
}

export default BookingSuccess