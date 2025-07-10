import { Request, Response, NextFunction } from 'express';
import { ZodObject, ZodRawShape } from 'zod';

export const validateVehicle = (schema: ZodObject<ZodRawShape>) => (
    req: Request,
    res: Response,
    next: NextFunction
): any => {
    const result = schema.strict().safeParse(req.body);

    if (!result.success) {
        return res.status(400).json({
            message: 'Validation Error',
            errors: result.error.format()
        })
    }

    req.body = result.data;
    next();
}