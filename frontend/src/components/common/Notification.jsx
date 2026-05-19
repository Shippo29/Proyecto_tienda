import React from "react";
import { useApp } from "../../hooks/useApp";
import "./Notification.css";

export default function Notification() {
  const { notification, clearNotification } = useApp();

  if (!notification) return null;

  const { message, type } = notification;

  return (
    <div className={`notification notification-${type}`}>
      <div className="notification-content">
        <p>{message}</p>
        <button onClick={clearNotification} className="notification-close">
          ✕
        </button>
      </div>
    </div>
  );
}
