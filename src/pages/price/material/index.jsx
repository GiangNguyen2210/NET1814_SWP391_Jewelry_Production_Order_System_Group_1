import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function PriceMaterial() {
  const [materialPrice, setMaterialPrice] = useState([]);
  const navigate = useNavigate();

  const fetchMaterialPrice = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/material/notGold`
      );
      setMaterialPrice(response.data.result);
      console.log(response.data.result);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchMaterialPrice();
  }, []);

  const handleNavigate = (path) => {
    navigate(path);
  };

  return (
    <div className="min-h-screen w-screen bg-[#434343] ">
      <div className="grid grid-cols-12 gap-4 pt-4">
        <div className="col-start-2 col-span-10 bg-gray-300 text-center p-1 rounded-lg">
          <h1 className="bg-gray-400 p-4 text-2xl">GIÁ VẬT LIỆU</h1>
          <div className="grid grid-cols-12 border">
            <div className="col-span-6 p-2 text-xl border">LOẠI VẬT LIỆU</div>
            <div className="col-span-3 p-2 text-xl border">GIÁ</div>
            <div className="col-span-3 p-2 text-xl border">
              THỜI ĐIỂM CẬP NHẬT
            </div>
          </div>
          {materialPrice.map((item, index) => (
            <div key={index} className="grid grid-cols-12 border">
              <div className="col-span-6 p-2 bg-white border text-left text-xl">
                {item.materialName}
              </div>
              <div className="col-span-3 p-2 bg-white border">
                {item.pricePerUnit}
              </div>
              <div className="col-span-3 p-2 bg-white border">
                {item.updateTime}
              </div>
            </div>
          ))}
        </div>
        <button
          className="col-start-10 col-span-2 bg-[#F7EF8A] text-xl p-4 font-bold rounded-lg"
          onClick={() => handleNavigate("/price/gold")}
        >
          GIÁ VÀNG
        </button>
      </div>
    </div>
  );
}

export default PriceMaterial;