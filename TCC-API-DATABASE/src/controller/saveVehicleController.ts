import { Request, Response } from 'express';
import { vehicleDataModel } from '../model/vehicleModel';

export const saveVehicleData = async (req: Request, res: Response) => {
    try {
        const data = new vehicleDataModel(req.body);
        await data.save();
        res.status(201).json({message: 'Success! Data saved!'});
    } catch (error) {
        res.status(500).json({ message: `Error to save data ${error}`});
    }
};