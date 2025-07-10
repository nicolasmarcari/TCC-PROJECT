import { RequestHandler } from "express";

const authentication: RequestHandler = (req, res, next) => {
    const token = req.headers['authorization'];

    if (!token) {
        res.status(401).json({ message: 'missing token' });
        return;
    }

    const parts = token.split(' ');
    const basicToken = parts.length === 2 ? parts[1] : token;

    if (basicToken !== process.env.SECRET_TOKEN) {
        res.status(401).json({ message: 'Unauthorized' });
        return;
    }

    next();
}

export default authentication;