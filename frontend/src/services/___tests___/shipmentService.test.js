import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getShipments, createShipment, getShipmentById, updateShipment, deleteShipment } from '../../../src/services/shipmentService'
import api from '../../../src/services/api'

vi.mock('../../../src/services/api')

describe('shipmentService', () => {
    beforeEach(() => {
    vi.clearAllMocks()
})

describe('getShipments', () => {
    it('retorna lista de envios', async () => {
        const mockShipments = [
        { id: 1, pedidoId: 10, direccion: 'Av. 123', estado: 'PENDIENTE' },
        ]
        api.get.mockResolvedValue({ data: mockShipments })

        const result = await getShipments()

        expect(result).toEqual(mockShipments)
        expect(api.get).toHaveBeenCalledWith('/envios')
    })

    it('retorna array vacio cuando API retorna null', async () => {
        api.get.mockResolvedValue({ data: null })

        const result = await getShipments()

        expect(result).toEqual([])
    })

    it('lanza error cuando API falla', async () => {
        api.get.mockRejectedValue(new Error('Network error'))

        await expect(getShipments()).rejects.toBeDefined()
    })
})

describe('createShipment', () => {
    it('crea un envio correctamente', async () => {
        const payload = { pedidoId: 10, direccion: 'Av. 123', estado: 'PENDIENTE' }
        const mockResponse = { id: 1, ...payload }
        api.post.mockResolvedValue({ data: mockResponse })

        const result = await createShipment(payload)

        expect(result).toEqual(mockResponse)
        expect(api.post).toHaveBeenCalledWith('/envios', payload)
    })

    it('lanza error cuando payload es invalido', async () => {
        await expect(createShipment(null)).rejects.toThrow()
    })

    it('lanza error cuando API falla', async () => {
        api.post.mockRejectedValue(new Error('Server error'))

        await expect(createShipment({ pedidoId: 1 })).rejects.toBeDefined()
    })
})

describe('getShipmentById', () => {
    it('retorna envio por id', async () => {
        const mockShipment = { id: 1, pedidoId: 10, estado: 'PENDIENTE' }
        api.get.mockResolvedValue({ data: mockShipment })

        const result = await getShipmentById(1)

        expect(result).toEqual(mockShipment)
        expect(api.get).toHaveBeenCalledWith('/envios/1')
    })
})

describe('updateShipment', () => {
    it('actualiza un envio correctamente', async () => {
        const payload = { pedidoId: 10, direccion: 'Av. 123', estado: 'EN_CAMINO' }
        const mockResponse = { id: 1, ...payload }
        api.put.mockResolvedValue({ data: mockResponse })

        const result = await updateShipment(1, payload)

        expect(result).toEqual(mockResponse)
        expect(api.put).toHaveBeenCalledWith('/envios/1', payload)
    })

    it('lanza error cuando la API falla al actualizar', async () => {
        api.put.mockRejectedValue(new Error('Server error'))

        await expect(updateShipment(1, { estado: 'ENTREGADO' })).rejects.toBeDefined()
    })
})

describe('deleteShipment', () => {
    it('elimina un envio correctamente', async () => {
        api.delete.mockResolvedValue({ data: null })

        await deleteShipment(1)

        expect(api.delete).toHaveBeenCalledWith('/envios/1')
    })

    it('lanza error cuando la API falla al eliminar', async () => {
        api.delete.mockRejectedValue(new Error('Server error'))

        await expect(deleteShipment(1)).rejects.toBeDefined()
    })
})
})