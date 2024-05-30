import React from "react";

function Footer() {
  return (
    <footer className="bg-black text-[#d6c33a] flex justify-around p-2">
      <div className="grid grid-cols-1 grid-rows-6 gap-1">
        <div>Chi nhánh</div>
        <div>Q12</div>
        <div>Bình Thạnh</div>
        <div>Biên Hòa</div>
        <div>Phú Nhuận</div>
        <div>Q3</div>
      </div>
      <div className="grid grid-cols-1 grid-rows-5 gap-1">
        <div>Phương thức thanh toán</div>
        <div>Mastercard</div>
        <div>Visa</div>
        <div>Momo</div>
      </div>
      <div className="grid grid-cols-1 grid-rows-5 gap-1">
        <div>Kết nối với chúng tôi</div>
        <div>Facebook</div>
        <div>Instagram</div>
        <div>Zalo</div>
      </div>
    </footer>
  );
}

export default Footer;
