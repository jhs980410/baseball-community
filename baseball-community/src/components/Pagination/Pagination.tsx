import React from "react";
import "./Pagination.css";

export default function Pagination(){

    return(
             <div className="pagination">
                    <button>&lt;</button>
                    {[1, 2, 3, 4, 5].map((num) => (
                      <button key={num}>{num}</button>
                    ))}
                    <button>&gt;</button>
            </div>
    );
}