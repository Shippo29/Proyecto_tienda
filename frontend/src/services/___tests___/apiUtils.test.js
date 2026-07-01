import { describe, it, expect, vi } from 'vitest'
import { requestApi, validatePayload } from '../../../src/services/apiUtils'

describe('apiUtils', () => {
describe('requestApi', () => {
    it('retorna data cuando la llamada es exitosa', async () => {
        const mockFn = vi.fn().mockResolvedValue({ data: { id: 1, nombre: 'Test' } })

        const result = await requestApi(mockFn, 'Error')

        expect(result).toEqual({ id: 1, nombre: 'Test' })
    })

    it('lanza error con mensaje del servidor cuando falla', async () => {
        const mockFn = vi.fn().mockRejectedValue({
        response: { data: { message: 'Error del servidor' } },
        })

        await expect(requestApi(mockFn, 'Error generico')).rejects.toMatchObject({
        message: 'Error del servidor',
        })
    })

    it('lanza error con mensaje generico cuando no hay mensaje del servidor', async () => {
        const mockFn = vi.fn().mockRejectedValue(new Error('Network error'))

        await expect(requestApi(mockFn, 'Error generico')).rejects.toMatchObject({
        message: expect.any(String),
        })
    })
})

describe('validatePayload', () => {
    it('retorna el payload cuando es valido', () => {
        const payload = { cliente: 'Juan', producto: 'Laptop' }
        const result = validatePayload(payload, 'Error')
        expect(result).toEqual(payload)
    })

    it('lanza error cuando payload es null', () => {
        expect(() => validatePayload(null, 'Payload invalido')).toThrow('Payload invalido')
    })

    it('lanza error cuando payload no es objeto', () => {
        expect(() => validatePayload('string', 'Payload invalido')).toThrow('Payload invalido')
    })

    it('lanza error cuando payload es undefined', () => {
        expect(() => validatePayload(undefined, 'Payload invalido')).toThrow('Payload invalido')
    })
    })
})