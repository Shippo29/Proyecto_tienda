import { describe, it, expect, vi, beforeEach } from 'vitest'
import { getProducts, getProductById } from '../../../src/services/productService'
import api from '../../../src/services/api'

vi.mock('../../../src/services/api')

describe('productService', () => {
    beforeEach(() => {
    vi.clearAllMocks()
    })

describe('getProducts', () => {
    it('retorna lista de productos cuando la API responde correctamente', async () => {
    const mockProducts = [
        { id: 1, nombre: 'Laptop', precio: 999.99, stock: 10 },
        { id: 2, nombre: 'Mouse', precio: 29.99, stock: 50 },
    ]
    api.get.mockResolvedValue({ data: mockProducts })

    const result = await getProducts()

    expect(result).toEqual(mockProducts)
    expect(api.get).toHaveBeenCalledWith('/productos')
    })

    it('retorna array vacio cuando la API retorna null', async () => {
    api.get.mockResolvedValue({ data: null })

    const result = await getProducts()

    expect(result).toEqual([])
    })

    it('lanza error cuando la API falla', async () => {
        api.get.mockRejectedValue(new Error('Network error'))

        await expect(getProducts()).rejects.toBeDefined()
    })
    })

    describe('getProductById', () => {
    it('retorna un producto por id', async () => {
        const mockProduct = { id: 1, nombre: 'Laptop', precio: 999.99, stock: 10 }
        api.get.mockResolvedValue({ data: mockProduct })

        const result = await getProductById(1)

        expect(result).toEqual(mockProduct)
        expect(api.get).toHaveBeenCalledWith('/productos/1')
    })

    it('lanza error cuando el producto no existe', async () => {
        api.get.mockRejectedValue({ response: { status: 404 } })

        await expect(getProductById(999)).rejects.toBeDefined()
    })
    })
})