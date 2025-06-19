import { Request, Response, NextFunction } from 'express';
import { ZodSchema } from 'zod';

export const validateVehicle = (schema: ZodSchema) => (
    req: Request,
    res: Response,
    next: NextFunction
): any => {
    const result = schema.safeParse(req.body);

    if (!result.success) {
        return res.status(400).json({
            message: 'Validation Error',
            errors: result.error
        })
    }

    req.body = result.data;
    next();
}