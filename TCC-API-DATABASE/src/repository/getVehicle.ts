import { vehicleDataModel } from "../model/vehicleModel";

export async function getVehicleByUUID(uuid: string) {
    try {
        const vehicle = await vehicleDataModel.findOne({ uuid });
        return vehicle
    } catch(e) {
        throw e;
    }
}