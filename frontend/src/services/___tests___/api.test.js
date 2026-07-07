import { describe, it, expect } from 'vitest'
import api from '../../../src/services/api'

describe('api (interceptors)', () => {
  const requestInterceptor = api.interceptors.request.handlers[0]
  const responseInterceptor = api.interceptors.response.handlers[0]

  describe('request interceptor', () => {
    it('retorna la configuracion sin modificarla cuando la solicitud es valida', () => {
      const config = {
        method: 'get',
        baseURL: 'http://localhost:8080',
        url: '/productos',
        data: null,
      }

      const result = requestInterceptor.fulfilled(config)

      expect(result).toBe(config)
    })

    it('retorna la configuracion sin loguear cuando NO esta en modo desarrollo', () => {
      const originalDev = import.meta.env.DEV
      import.meta.env.DEV = false

      const config = {
        method: 'post',
        baseURL: 'http://localhost:8080',
        url: '/pedidos',
        data: { cliente: 'Juan' },
      }

      const result = requestInterceptor.fulfilled(config)

      expect(result).toBe(config)
      import.meta.env.DEV = originalDev
    })

    it('propaga el error cuando falla la configuracion de la solicitud', async () => {
      const error = new Error('request error')

      await expect(requestInterceptor.rejected(error)).rejects.toThrow('request error')
    })
  })

  describe('response interceptor', () => {
    it('retorna la respuesta sin modificarla cuando la peticion es exitosa', () => {
      const response = {
        status: 200,
        config: { url: '/productos' },
        data: [{ id: 1, nombre: 'Producto' }],
      }

      const result = responseInterceptor.fulfilled(response)

      expect(result).toBe(response)
    })

    it('retorna la respuesta sin loguear cuando NO esta en modo desarrollo', () => {
      const originalDev = import.meta.env.DEV
      import.meta.env.DEV = false

      const response = {
        status: 200,
        config: { url: '/envios' },
        data: [],
      }

      const result = responseInterceptor.fulfilled(response)

      expect(result).toBe(response)
      import.meta.env.DEV = originalDev
    })

    it('propaga el error cuando el servidor responde con un error', async () => {
      const error = { response: { status: 500, data: { message: 'Error interno' } } }

      await expect(responseInterceptor.rejected(error)).rejects.toBe(error)
    })

    it('propaga el error cuando no hay respuesta del servidor', async () => {
      const error = { request: {} }

      await expect(responseInterceptor.rejected(error)).rejects.toBe(error)
    })

    it('propaga el error cuando falla la configuracion de la solicitud', async () => {
      const error = { message: 'Config error' }

      await expect(responseInterceptor.rejected(error)).rejects.toBe(error)
    })
  })
})
