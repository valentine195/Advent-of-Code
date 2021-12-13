import { readFile, writeFile } from "fs/promises";
import fetch from "axios";
import path from "path";

import { config } from "dotenv";
config();

export interface InputOptions {
    day: number;
    year?: number;
    strip?: boolean;
}

export class Input {
    static path = path.resolve(`inputs`);
    constructor(private content: string) {}

    public static async get(options: InputOptions): Promise<Input> {
        const day = options.day;
        const year = options.year ?? 2021;
        const file = `day${day}.txt`;
        console.log("🚀 ~ file: advent.ts ~ line 21 ~ file", file);
        const url = `https://adventofcode.com/${options.year}/day/${options.day}/input`;

        try {
            const buffer = await readFile(path.join(Input.path, file));
            console.log(`Using input from ${file}`);
            return new Input(buffer.toString().trim().replace("\r", ""));
        } catch (error) {
            if (
                error instanceof Error &&
                "code" in error &&
                (error as any).code === "ENOENT" &&
                url !== undefined &&
                process.env.session
            ) {
                console.log("Downloading input...");
                const response = await fetch.get(
                    `https://adventofcode.com/${year}/day/${day}/input`,
                    {
                        headers: {
                            cookie: `session=${process.env.session}`
                        }
                    }
                );

                if (response.status !== 200) {
                    console.log(response.data);
                    throw new Error(
                        `Failed to fetch input: ${response.status}`
                    );
                }

                const content = response.data;
                await writeFile(path.join(Input.path, file), content);
                return new Input(content.trim().replace("\r", ""));
            }
            throw error;
        }
    }

    public all(): string {
        return this.content;
    }

    public lines(): string[] {
        return this.content.split("\n");
    }

    public forLines<T>(fn: (input: Input) => T): T[] {
        return this.lines().map((line) => fn(new Input(line)));
    }

    public tokens(separator: RegExp = /[\s\n]+/g): string[] {
        return this.content.split(separator);
    }

    public lineTokens(
        lineSeparator: RegExp = /\n/g,
        tokenSeparator: RegExp = /[\s]+/g
    ): string[][] {
        return this.content
            .split(lineSeparator)
            .map((line) => line.split(tokenSeparator));
    }

    public ints(): number[] {
        return this.tokens().map(int);
    }

    public nums(): number[] {
        return this.tokens().map(num);
    }
}
export const int = (x: string): number => parseInt(x, 10);
export const num = (x: string): number => parseFloat(x);
export const str = (x: any): string => x.toString();
export const bool = (x: any): boolean => !!x;
