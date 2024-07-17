import LatestTransaction from "./transactions";
import SaleByCategory from "./category";
import TopSelling from "./top-selling";
import SaleTrend from "./trend";

function Dashboard() {
  return (
    <div className="w-screen min-h-screen bg-gray-100">
      <div className="grid grid-cols-12 gap-4 m-4">
        <div className="col-start-2 col-span-6">
          <SaleTrend />
        </div>
        <div className="col-start-8 col-span-4">
          <LatestTransaction />
        </div>
      </div>
      <div className="grid grid-cols-12 gap-4">
        <div className="col-start-2 col-span-5">
          <SaleByCategory />
        </div>
        <div className="col-start-7 col-span-5 ">
          <TopSelling />
        </div>
      </div>
    </div>
  );
}

export default Dashboard;