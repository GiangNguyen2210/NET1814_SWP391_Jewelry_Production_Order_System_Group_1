import LatestTransaction from "./transactions";
import TopSelling from "./top-selling";
import SaleTrend from "./trend";
import Overview from "./overview";
import Navbar from "../../components/navbar";
import { Link, useNavigate } from "react-router-dom";
import LogoutButton from "../../components/logoutButton";
import { useEffect } from "react";
import authorService from "../../services/authorService";

function Dashboard() {
  const navigate = useNavigate();

  useEffect(() => {
    if (!authorService.checkPermission("MANAGER")) {
      navigate("/unauthorized");
    }
  }, [navigate]);

  return (
    <div className="w-screen min-h-screen bg-gray-300">
      <div className="bg-[#1d1d1d] text-white h-40 flex justify-between items-center px-10">
        <Link to={"/"}>
          <img
            className="h-[160px] w-auto"
            src="/src/assets/images/logo.png"
            alt="Logo"
          />
        </Link>
        <div className="flex-grow text-center">
          <h1 className="text-5xl">QUẢN LÝ</h1>
        </div>
        <div className="w-80 text-right">
          <LogoutButton />
        </div>
      </div>
      <Navbar />
      <div className="grid grid-cols-12 gap-4">
        <div className="col-span-full">
          <Overview />
        </div>
        <div className="col-span-full">
          <SaleTrend />
        </div>
        <div className="col-span-7">
          <LatestTransaction />
        </div>
        <div className="col-span-5">
          <TopSelling />
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
