import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getOrders, createOrder, getOrderById, deleteOrder, updateOrder } from '../../../src/services/orderService'
import api from '../../../src/services/api'

vi.mock('../../../src/services/api')

describe('orderService', () => {
    beforeEach(() => {
    vi.clearAllMocks()
})

describe('getOrders', () => {
    it('retorna lista de pedidos', async () => {
        const mockOrders = [
        { id: 1, cliente: 'Juan', producto: 'Laptop', cantidad: 1, total: 999.99 },
        ]
        api.get.mockResolvedValue({ data: mockOrders })

        const result = await getOrders()

        expect(result).toEqual(mockOrders)
        expect(api.get).toHaveBeenCalledWith('/pedidos')
    })

    it('retorna array vacio cuando API retorna null', async () => {
        api.get.mockResolvedValue({ data: null })

        const result = await getOrders()

        expect(result).toEqual([])
    })

    it('lanza error cuando API falla', async () => {
        api.get.mockRejectedValue(new Error('Network error'))

        await expect(getOrders()).rejects.toBeDefined()
    })
})

describe('createOrder', () => {
    it('crea un pedido correctamente', async () => {
        const payload = { cliente: 'Juan', producto: 'Laptop', cantidad: 1, total: 999.99 }
        const mockResponse = { id: 1, ...payload }
        api.post.mockResolvedValue({ data: mockResponse })

        const result = await createOrder(payload)

        expect(result).toEqual(mockResponse)
        expect(api.post).toHaveBeenCalledWith('/pedidos', payload)
    })

    it('lanza error cuando el payload es invalido', async () => {
        await expect(createOrder(null)).rejects.toThrow()
    })

    it('lanza error cuando la API falla', async () => {
        api.post.mockRejectedValue(new Error('Server error'))

        await expect(createOrder({ cliente: 'Juan', producto: 'Laptop' })).rejects.toBeDefined()
    })
})

describe('getOrderById', () => {
    it('retorna pedido por id', async () => {
        const mockOrder = { id: 1, cliente: 'Juan', producto: 'Laptop' }
        api.get.mockResolvedValue({ data: mockOrder })

        const result = await getOrderById(1)

        expect(result).toEqual(mockOrder)
        expect(api.get).toHaveBeenCalledWith('/pedidos/1')
    })
})

describe('deleteOrder', () => {
    it('elimina un pedido correctamente', async () => {
        api.delete.mockResolvedValue({ data: null })

        await deleteOrder(1)

        expect(api.delete).toHaveBeenCalledWith('/pedidos/1')
    })
    })

describe('updateOrder', () => {
    it('actualiza un pedido correctamente', async () => {
        const payload = { cliente: 'Juan', producto: 'Laptop', cantidad: 2, total: 1999.98 }
        const mockResponse = { id: 1, ...payload }
        api.put.mockResolvedValue({ data: mockResponse })

        const result = await updateOrder(1, payload)

        expect(result).toEqual(mockResponse)
        expect(api.put).toHaveBeenCalledWith('/pedidos/1', payload)
    })

    it('lanza error cuando la API falla al actualizar', async () => {
        api.put.mockRejectedValue(new Error('Server error'))

        await expect(updateOrder(1, { cliente: 'Juan' })).rejects.toBeDefined()
    })
})
})