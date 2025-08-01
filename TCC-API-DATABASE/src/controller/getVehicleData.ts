import { Request, Response } from "express";
import { getVehicleByUUID } from "../repository/getVehicle";

export const getVehicle = async (req: Request, res: Response) => {
    const { id } = req.params
    const vehicle = await getVehicleByUUID(id)
    if (vehicle) {
        res.status(200).send(vehicle);
    } else {
        res.status(404).send('vehicle not found');
    }

}