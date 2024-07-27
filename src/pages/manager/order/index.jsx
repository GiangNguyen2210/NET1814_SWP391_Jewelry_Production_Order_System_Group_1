import { Button, Form, Modal, Select, message } from "antd";
import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axiosInstance from "../../../services/axiosInstance";
import LogoutButton from "../../../components/logoutButton";
import Navbar from "../../../components/navbar";
import authorService from "../../../services/authorService";

function ManagerOrder() {
  const [isOpenModal, setOpenModal] = useState(false);
  const [orderList, setOrderList] = useState([]);
  const [designStaffList, setDesignStaffList] = useState([]);
  const [productionStaffList, setProductionStaffList] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const navigate = useNavigate();

  const fetchData = async () => {
    try {
      const [orderResponse, designResponse, productionResponse] =
        await Promise.all([
          axiosInstance.get("request-orders/getAllNewRequestOrder"),
          axiosInstance.get("request-orders/getUserByRole/DESIGN_STAFF"),
          axiosInstance.get("request-orders/getUserByRole/PRODUCTION_STAFF"),
        ]);

      setOrderList(orderResponse.data.result);
      setDesignStaffList(designResponse.data.result);
      setProductionStaffList(productionResponse.data.result);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    if (authorService.checkPermission("MANAGER")) {
      fetchData();
    } else {
      navigate("/unauthorized");
    }
  }, [navigate]);

  const handleShowModal = (order) => {
    setSelectedOrder(order);
    setOpenModal(true);
  };

  const handleHideModal = () => {
    setOpenModal(false);
    setSelectedOrder(null);
  };

  const handleSubmit = async (values) => {
    try {
      const designStaffId =
        selectedOrder?.designID === null ? values.designStaff : "0";
      const url = `request-orders/${selectedOrder.id}/${designStaffId}/${values.productionStaff}`;
      await axiosInstance.put(url);
      message.success("Phân công thành công");
      handleHideModal();
    } catch (error) {
      console.error("Error assigning staff:", error);
      message.error("Phân công thất bại");
    }
  };

  const handleNavigateClick = (path) => {
    if (location.pathname !== path) {
      navigate(path);
    }
  };

  return (
    <div className="bg-[#434343] min-h-screen w-screen">
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
      <h1 className="text-center text-[#F7EF8A] text-4xl font-bold">
        QUẢN LÝ ĐƠN HÀNG
      </h1>
      <div className="flex justify-center gap-1 mb-1 text-white">
        <button
          onClick={() => handleNavigateClick("/manager/request")}
          className="w-1/3 p-1 rounded-lg bg-blue-400 hover:bg-blue-500"
        >
          QUẢN LÝ YÊU CẦU
        </button>
        <button
          onClick={() => handleNavigateClick("/manager/order")}
          className="w-1/3 p-1 rounded-lg bg-blue-400 hover:bg-blue-500"
        >
          QUẢN LÝ ĐƠN HÀNG
        </button>
      </div>
      <div className="grid grid-cols-3 w-3/4 mx-auto bg-gray-300 p-4 rounded-lg">
        <div className="col-span-1 bg-gray-400 p-2 font-bold text-center">
          ID ĐƠN HÀNG
        </div>
        <div className="col-span-2 bg-gray-400 p-2 font-bold text-center">
          CHỈ ĐỊNH CÔNG VIỆC
        </div>
        {orderList.map((item) => (
          <React.Fragment key={item.id}>
            <div className="col-span-1 border p-2 text-center bg-white">
              {item.id}
            </div>
            <div className="col-span-2 border p-2 text-center bg-white">
              <Button type="link" onClick={() => handleShowModal(item)}>
                Chọn nhân viên
              </Button>
            </div>
          </React.Fragment>
        ))}
      </div>
      <Modal
        title="CHỈ ĐỊNH CÔNG VIỆC"
        open={isOpenModal}
        onCancel={handleHideModal}
        footer={null}
      >
        <Form
          initialValues={{
            designStaff: "",
            productionStaff: "",
          }}
          onFinish={handleSubmit}
        >
          {selectedOrder?.designID === null && (
            <Form.Item
              name="designStaff"
              label="Nhân viến thiết kế"
              rules={[
                {
                  required: true,
                  message: "Vui lòng chọn nhân viên thiết kế",
                },
              ]}
            >
              <Select>
                {designStaffList.map((staff) => (
                  <Select.Option key={staff.id} value={staff.id}>
                    {staff.userName}
                  </Select.Option>
                ))}
              </Select>
            </Form.Item>
          )}
          <Form.Item
            name="productionStaff"
            label="Nhân viên gia công"
            rules={[
              {
                required: true,
                message: "Vui lòng chọn nhân viên gia công",
              },
            ]}
          >
            <Select>
              {productionStaffList.map((staff) => (
                <Select.Option key={staff.id} value={staff.id}>
                  {staff.userName}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              CHỈ ĐỊNH
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

export default ManagerOrder;
