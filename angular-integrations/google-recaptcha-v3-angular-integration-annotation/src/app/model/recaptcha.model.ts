

export interface RecaptChaResponse {

    success: boolean,
    challege_ts: string,
    hostname: string,
    score: number,
    action: string,
    errorCodes: string[]

}