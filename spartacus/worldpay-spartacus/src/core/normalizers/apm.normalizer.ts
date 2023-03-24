import { Injectable } from '@angular/core';
import { Converter, Image, OccConfig } from '@spartacus/core';
import { ApmData, OccApmData, OccApmDataConfiguration, PaymentMethod } from '../interfaces';

@Injectable({
  providedIn: 'root'
})
export class ApmNormalizer implements Converter<OccApmData, ApmData> {

  constructor(protected config: OccConfig) {
  }

  convert(source: OccApmData, target?: ApmData): ApmData {
    const config = source.apmConfiguration;
    if (target === undefined) {
      target = {code: config.code, name: config.name};
    }

    target.code = config.code;
    target.name = config.name;

    if (source.media) {
      target.media =
        {
          mobile: {
            url: this.normalizeImageUrl(source.media.url),
            altText: config.name
          } as Image
        };
    }

    target.bankConfigurations = config.bankConfigurations?.map(({bankCode, bankName}) => ({
      code: bankCode,
      name: bankName
    }));

    return target;
  }

  /** taken from product-image-normalizer.ts
   * Traditionally, in an on-prem world, medias and other backend related calls
   * are hosted at the same platform, but in a cloud setup, applications are are
   * typically distributed cross different environments. For media, we use the
   * `backend.media.baseUrl` by default, but fallback to `backend.occ.baseUrl`
   * if none provided.
   */
  private normalizeImageUrl(url: string): string {
    if (new RegExp(/^(http|data:image|\/\/)/i).test(url)) {
      return url;
    }
    return (
      (this.config.backend.media.baseUrl ||
        this.config.backend.occ.baseUrl ||
        '') + url
    );
  }
}