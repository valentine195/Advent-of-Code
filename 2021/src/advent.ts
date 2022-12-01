import { readFile, writeFile, access } from "fs/promises";
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
        const url = `https://adventofcode.com/${year}/day/${day}/input`;

        return new Promise(
            async (resolve) =>
                await access(path.join(Input.path, file))
                    .then(async () => {
                        resolve(await Input.read(file));
                    })
                    .catch(async () => {
                        resolve(await Input.write(file, url));
                    })
        );
    }

    private static async write(file: string, url: string) {
        console.log("Downloading input...");
        const response = await fetch.get(url, {
            headers: {
                cookie: `session=${process.env.session}`
            }
        });

        if (response.status !== 200) {
            console.log(response.data);
            throw new Error(`Failed to fetch input: ${response.status}`);
        }

        const content = response.data;
        await writeFile(path.join(Input.path, file), content);
        return new Input(content.trim().replace("\r", ""));
    }
    private static async read(file: string) {
        const buffer = await readFile(path.join(Input.path, file));
        console.log(`Using input from ${file}`);
        return new Input(buffer.toString().trim().replace("\r", ""));
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
