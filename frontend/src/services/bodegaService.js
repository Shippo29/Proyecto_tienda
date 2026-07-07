import api from "./api";
import { requestApi } from "./apiUtils";

export const getBodegas = async () => {
    return requestApi(
        () => api.get("/bodegas"),
        "Error al cargar bodegas",
        ).then((data) => data || []);
};

export default { getBodegas };