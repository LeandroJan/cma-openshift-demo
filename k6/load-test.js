import http from "k6/http";
import { sleep, check } from "k6";

export let options = {
  vus: 100,
  duration: "5m",
  insecureSkipTLSVerify: true,
  thresholds: {
    http_req_duration: ["p(95)<1500"],
  },
};

export default function () {
  const res = http.get(
    "https://cma-openshift-demo-cma.apps.meu-cluster.sandbox2134.opentlc.com/api/products",
  );
  check(res, { "status was 200": (r) => r.status === 200 });
  sleep(0.1);
}
