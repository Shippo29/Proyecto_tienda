import React from "react";
import { AppProvider } from "./contexts/AppContext";
import AppRouter from "./router/AppRouter";
import Notification from "./components/common/Notification";

export default function App() {
  return (
    <AppProvider>
      <div className="app">
        <AppRouter />
        <Notification />
      </div>
    </AppProvider>
  );
}

