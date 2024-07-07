import { initializeApp } from "firebase/app";
import { getStorage } from "firebase/storage";
import { GoogleAuthProvider, getAuth } from "firebase/auth";

// const firebaseConfig = {
//   apiKey: "AIzaSyAMReUPanJVDBbQ6U1-5GdyoazFH3Xee6o",
//   authDomain: "jewelry-production.firebaseapp.com",
//   projectId: "jewelry-production",
//   storageBucket: "jewelry-production.appspot.com",
//   messagingSenderId: "1075441933073",
//   appId: "1:1075441933073:web:ec2d529a0e15d749eecab0",
//   measurementId: "G-E1YN8Z8DQ2",
// };

const firebaseConfig = {
  apiKey: "AIzaSyDPbts0vYP7J0yLvxyLse7cAj23AXxMN2A",
  authDomain: "jewelry-production-a025c.firebaseapp.com",
  projectId: "jewelry-production-a025c",
  storageBucket: "jewelry-production-a025c.appspot.com",
  messagingSenderId: "153726647894",
  appId: "1:153726647894:web:a3e8ffbd7f48bd84a8c5f7",
  measurementId: "G-MCC5DN2SC6",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const storage = getStorage(app);
const provider = new GoogleAuthProvider();
const auth = getAuth();
export { storage, provider, auth };