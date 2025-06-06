/* tslint:disable */
/* eslint-disable */
/**
 * MobyLab Web App
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
import type { UserDTO } from './UserDTO';
import {
    UserDTOFromJSON,
    UserDTOFromJSONTyped,
    UserDTOToJSON,
    UserDTOToJSONTyped,
} from './UserDTO';
import type { ErrorMessage } from './ErrorMessage';
import {
    ErrorMessageFromJSON,
    ErrorMessageFromJSONTyped,
    ErrorMessageToJSON,
    ErrorMessageToJSONTyped,
} from './ErrorMessage';

/**
 * 
 * @export
 * @interface UserDTORequestResponse
 */
export interface UserDTORequestResponse {
    /**
     * 
     * @type {UserDTO}
     * @memberof UserDTORequestResponse
     */
    readonly response?: UserDTO | null;
    /**
     * 
     * @type {ErrorMessage}
     * @memberof UserDTORequestResponse
     */
    readonly errorMessage?: ErrorMessage | null;
}

/**
 * Check if a given object implements the UserDTORequestResponse interface.
 */
export function instanceOfUserDTORequestResponse(value: object): value is UserDTORequestResponse {
    return true;
}

export function UserDTORequestResponseFromJSON(json: any): UserDTORequestResponse {
    return UserDTORequestResponseFromJSONTyped(json, false);
}

export function UserDTORequestResponseFromJSONTyped(json: any, ignoreDiscriminator: boolean): UserDTORequestResponse {
    if (json == null) {
        return json;
    }
    return {
        
        'response': json['response'] == null ? undefined : UserDTOFromJSON(json['response']),
        'errorMessage': json['errorMessage'] == null ? undefined : ErrorMessageFromJSON(json['errorMessage']),
    };
}

export function UserDTORequestResponseToJSON(json: any): UserDTORequestResponse {
    return UserDTORequestResponseToJSONTyped(json, false);
}

export function UserDTORequestResponseToJSONTyped(value?: Omit<UserDTORequestResponse, 'response'|'errorMessage'> | null, ignoreDiscriminator: boolean = false): any {
    if (value == null) {
        return value;
    }

    return {
        
    };
}

