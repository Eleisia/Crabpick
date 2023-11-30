import React, { useState } from "react";
import { Navigate, useNavigate } from "react-router-dom";
import "./DropDown.css";
  
const DropdownTrigger = (searchList: any) => {
  const navigate = useNavigate();

  function goToDetail(id: number) {
    navigate(`/detail/${id}`);
  }

  return (
    <div>
      {searchList.filteredGameList?.map((item: any, i: number) => {
        return (
          <div
            key={i}
            className="gameSearch"
            onClick={() => goToDetail(item.appId)}
          >
            <img
              className="gameIcon"
              src={`https://cdn.cloudflare.steamstatic.com/steam/apps/${item.appId}/capsule_sm_120.jpg?`}
              alt=""
            />
            <div className="gameName">{item.name}</div>
          </div>
        );
      })}
    </div>
  );
};

export default DropdownTrigger;
